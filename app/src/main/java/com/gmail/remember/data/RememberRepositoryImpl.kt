package com.gmail.remember.data

import androidx.core.app.ComponentActivity
import com.gmail.remember.R
import com.gmail.remember.data.datastore.models.auth.AuthPrefsModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

internal class RememberRepositoryImpl @Inject constructor(
    private val authPrefsModel: AuthPrefsModel
) : RememberRepository {
    override val token: Flow<String>
        get() = authPrefsModel.token.flowOn(Dispatchers.IO)

    override val firebaseAuth: FirebaseAuth
        get() = Firebase.auth

    override fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit) {
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

    override fun auth(token: String, task: (Task<AuthResult>) -> Unit) {
        val credentials = GoogleAuthProvider.getCredential(token, null)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener { task ->
            task(task)
        }
    }

    override suspend fun saveToken(token: String) {
        authPrefsModel.saveToken(token = token)
    }
}