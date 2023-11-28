package com.gmail.remember.data

import androidx.core.app.ComponentActivity
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.FRIDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.MONDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.SATURDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.SUNDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.THURSDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.TUESDAY
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.WEDNESDAY
import com.gmail.remember.R
import com.gmail.remember.data.api.DictionaryApi
import com.gmail.remember.data.api.models.dictionary.DictionaryRs
import com.gmail.remember.data.creator.ServiceCreator
import com.gmail.remember.data.datastore.models.auth.ProfilePrefsModel
import com.gmail.remember.domain.toModel
import com.gmail.remember.models.DayModel
import com.gmail.remember.models.ProfileModel
import com.gmail.remember.models.ProfileSettingsModel
import com.gmail.remember.models.ThemeModel
import com.gmail.remember.models.WordModel
import com.gmail.remember.utils.toDayName
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val THEMES = "themes"
private const val PROFILE = "profile"
private const val COUNT = "countSuccess"
private const val ALL_DAYS = "allDays"
private const val IS_REMEMBER = "remember"
private const val DAYS = "days"
private const val CHECK_DAY = "check"

internal class RememberRepositoryImpl @Inject constructor(
    private val authPrefsModel: ProfilePrefsModel,
    private val serviceCreator: ServiceCreator
) : RememberRepository {

    private val dataBase = Firebase.database
    override val settingsProfile: Flow<ProfileSettingsModel>
        get() = dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE).snapshots.map { data ->
                data.getValue(ProfileSettingsModel::class.java) ?: ProfileSettingsModel()
            }

            .flowOn(Dispatchers.IO)
    override val profile: Flow<ProfileModel>
        get() = authPrefsModel.profileModel.flowOn(Dispatchers.IO)

    override val firebaseAuth: FirebaseAuth
        get() = Firebase.auth
    override val themes: Flow<List<ThemeModel>>
        get() = dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(THEMES)
            .snapshots.combine(
                dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
                    .child(PROFILE)
                    .child(COUNT)
                    .snapshots
            ) { themes, count ->
                themes.children.map { children ->
                    children.toModel(count.value.toString().toInt())
                }
            }
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
            .child(PROFILE)
            .setValue(profileModel.toModel())
    }

    override suspend fun getWordFromDictionary(word: String): DictionaryRs? {
        return try {
            serviceCreator.create(DictionaryApi::class).getWord(word = word)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addWord(model: WordModel, childName: String): Task<Void> =
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(THEMES)
            .child(childName)
            .child(model.wordEng)
            .setValue(model)


    override suspend fun deleteWords(models: List<WordModel?>, childName: String) {
        models.forEach { model ->
            dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
                .child(THEMES)
                .child(childName)
                .child(model?.wordEng ?: "")
                .removeValue()
        }
    }

    override fun words(childName: String): Flow<DataSnapshot> =
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(THEMES)
            .child(childName)
            .snapshots
            .flowOn(Dispatchers.IO)

    override suspend fun addSection(name: String) {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(THEMES)
            .child(name)
            .setValue(name)
    }

    override suspend fun deleteSection(name: String) {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(THEMES)
            .child(name)
            .removeValue()
    }

    override suspend fun onCheckedChangeAllDays(value: Boolean) {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE)
            .child(ALL_DAYS)
            .setValue(value)
    }

    override suspend fun onCheckedChangeRemember(value: Boolean) {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE)
            .child(IS_REMEMBER)
            .setValue(value)
    }

    override suspend fun checkDay(name: String) {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE)
            .child(DAYS)
            .child(name)
            .child(CHECK_DAY)
            .setValue(true)
    }

    override suspend fun unCheckDay(name: String) {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE)
            .child(DAYS)
            .child(name)
            .child(CHECK_DAY)
            .setValue(false)
    }

    override suspend fun unCheckAllDays() {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE)
            .child(DAYS)
            .setValue(
                hashMapOf(
                    MONDAY.toDayName() to DayModel(
                        name = MONDAY.toDayName(),
                        check = false,
                        id = 0
                    ),
                    TUESDAY.toDayName() to DayModel(
                        name = TUESDAY.toDayName(),
                        check = false,
                        id = 1
                    ),
                    WEDNESDAY.toDayName() to DayModel(
                        name = WEDNESDAY.toDayName(),
                        check = false,
                        id = 2
                    ),
                    THURSDAY.toDayName() to DayModel(
                        name = THURSDAY.toDayName(),
                        check = false,
                        id = 3
                    ),
                    FRIDAY.toDayName() to DayModel(
                        name = FRIDAY.toDayName(),
                        check = false,
                        id = 4
                    ),
                    SATURDAY.toDayName() to DayModel(
                        name = SATURDAY.toDayName(),
                        check = false,
                        id = 5
                    ),
                    SUNDAY.toDayName() to DayModel(
                        name = SUNDAY.toDayName(),
                        check = false,
                        id = 6
                    ),
                )
            )
    }

    override suspend fun checkAllDays() {
        dataBase.getReference(firebaseAuth.currentUser?.uid ?: "")
            .child(PROFILE)
            .child(DAYS)
            .setValue(
                hashMapOf(
                    MONDAY.toDayName() to DayModel(name = MONDAY.toDayName(), check = true, id = 0),
                    TUESDAY.toDayName() to DayModel(
                        name = TUESDAY.toDayName(),
                        check = true,
                        id = 1
                    ),
                    WEDNESDAY.toDayName() to DayModel(
                        name = WEDNESDAY.toDayName(),
                        check = true,
                        id = 2
                    ),
                    THURSDAY.toDayName() to DayModel(
                        name = THURSDAY.toDayName(),
                        check = true,
                        id = 3
                    ),
                    FRIDAY.toDayName() to DayModel(name = FRIDAY.toDayName(), check = true, id = 4),
                    SATURDAY.toDayName() to DayModel(
                        name = SATURDAY.toDayName(),
                        check = true,
                        id = 5
                    ),
                    SUNDAY.toDayName() to DayModel(name = SUNDAY.toDayName(), check = true, id = 6),
                )
            )
    }
}