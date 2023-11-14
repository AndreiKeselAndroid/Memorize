package com.gmail.remember.data

import androidx.core.app.ComponentActivity
import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.RememberWordModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface RememberRepository {

    val profile: Flow<ProfileModel>

    val firebaseAuth: FirebaseAuth

    val words: Flow<DataSnapshot>
    suspend fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit)

    suspend fun auth(token: String, task: (Task<AuthResult>) -> Unit)

    suspend fun saveProfile(profileModel: ProfileModel)

    suspend fun getWordFromDictionary(word: String): DictionaryRs?

    suspend fun addWord(model: RememberWordModel): Task<Void>

    suspend fun deleteWords(models: List<RememberWordModel?>)
}