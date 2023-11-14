package com.gmail.remember.data

import androidx.core.app.ComponentActivity
import com.gmail.remember.R
import com.gmail.remember.data.api.DictionaryApi
import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import com.gmail.remember.data.creator.ServiceCreator
import com.gmail.remember.data.datastore.models.auth.ProfilePrefsModel
import com.gmail.remember.domain.toModel
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.RememberWordModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val WORDS = "words"
private const val INFO = "info"

internal class RememberRepositoryImpl @Inject constructor(
    private val authPrefsModel: ProfilePrefsModel,
    private val serviceCreator: ServiceCreator
) : RememberRepository {

    private val dataBase = Firebase.database
    override val profile: Flow<ProfileModel>
        get() = authPrefsModel.profileModel.flowOn(Dispatchers.IO)

    override val firebaseAuth: FirebaseAuth
        get() = Firebase.auth

    override val words: Flow<DataSnapshot>
        get() = dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(WORDS)
            .snapshots
            .flowOn(Dispatchers.IO)

    override suspend fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit) {
        launch(
            GoogleSignIn.getClient(
                activity,
                GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(activity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            )
        )
    }

    override suspend fun auth(token: String, task: (Task<AuthResult>) -> Unit) {
        val credentials = GoogleAuthProvider.getCredential(token, null)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener { tasks ->
            task(tasks)
        }
    }

    override suspend fun saveProfile(profileModel: ProfileModel) {
        authPrefsModel.saveProfile(profileModel = profileModel)
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(INFO)
            .setValue(profileModel.toModel())
    }

    override suspend fun getWordFromDictionary(word: String): DictionaryRs? {
        return try {
            serviceCreator.create(DictionaryApi::class).getWord(word = word)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addWord(model: RememberWordModel): Task<Void> =
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(WORDS)
            .child(model.wordEng)
            .setValue(model)


    override suspend fun deleteWords(models: List<RememberWordModel?>) {
        models.forEach { model ->
            dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
                .child(WORDS)
                .child(model?.wordEng ?: "")
                .removeValue()
        }
    }
}