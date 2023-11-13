package com.gmail.remember.data

import androidx.core.app.ComponentActivity
import com.gmail.remember.data.api.models.DictionaryRs
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.RememberWordModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

interface RememberRepository {

    val profile: Flow<ProfileModel>

    val firebaseAuth: FirebaseAuth

    fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit)

    fun auth(token: String, task: (Task<AuthResult>) -> Unit)

    suspend fun saveProfile(profileModel: ProfileModel)

    suspend fun getWordFromDictionary(word: String): DictionaryRs?

    fun addWord(model: RememberWordModel): Task<Void>

    fun deleteWord(model: RememberWordModel): Task<Void>
}