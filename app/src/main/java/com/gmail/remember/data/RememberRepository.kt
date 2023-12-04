package com.gmail.remember.data

import androidx.core.app.ComponentActivity
import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.ThemeModel
import com.gmail.remember.models.WordModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface RememberRepository {

    val settingsProfile: Flow<ProfileSettingsModel>

    val firebaseAuth: FirebaseAuth

    val themes: Flow<List<ThemeModel>>

    fun words(childName: String): Flow<DataSnapshot>

    suspend fun signIn(activity: ComponentActivity, launch: (GoogleSignInClient) -> Unit)

    suspend fun auth(token: String, task: (Task<AuthResult>) -> Unit)

    suspend fun saveProfile(profileModel: ProfileSettingsModel)

    suspend fun getWordFromDictionary(word: String): DictionaryRs?

    suspend fun addWord(model: WordModel, childName: String): Task<Void>

    suspend fun deleteWords(models: List<WordModel?>, childName: String)
    suspend fun addSection(name: String)
    suspend fun deleteSection(name: String)
    suspend fun onCheckedChangeAllDays(value: Boolean)
    suspend fun onCheckedChangeRemember(value: Boolean)
    suspend fun checkDay(name: String)
    suspend fun unCheckDay(name: String)
    suspend fun unCheckAllDays()
    suspend fun checkAllDays()
    suspend fun checkTheme(name: String)
    suspend fun setTimeFrom(time: String)
    suspend fun setTimeTo(time: String)
}