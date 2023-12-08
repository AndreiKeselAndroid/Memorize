package com.gmail.remember.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.remember.data.RememberData.listLevels
import com.gmail.remember.data.RememberData.listTime
import com.gmail.remember.data.api.models.Result
import com.gmail.remember.data.api.models.asResult
import com.gmail.remember.domain.usercases.ProfileUserCase
import com.gmail.remember.models.DayModel
import com.gmail.remember.models.LevelModel
import com.gmail.remember.models.ProgressModel
import com.gmail.remember.models.ThemeModel
import com.gmail.remember.models.TimeModel
import com.gmail.remember.models.WordModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

const val DEFAULT_COUNT_THEMES = 1

@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val profileUserCase: ProfileUserCase,
) : ViewModel() {

    private val listTimes: MutableStateFlow<List<TimeModel>> = MutableStateFlow(listTime)

    private val _levels: MutableStateFlow<List<LevelModel>> = MutableStateFlow(listLevels)

    private val _countThemes: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_COUNT_THEMES)
    val countThemes: StateFlow<Int> = _countThemes.asStateFlow()

    private val _expandTimeFrom: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val expandTimeFrom: StateFlow<Boolean> = _expandTimeFrom.asStateFlow()

    private val _expandTimeTo: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val expandTimeTo: StateFlow<Boolean> = _expandTimeTo.asStateFlow()

    private val _expandLevels: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val expandLevels: StateFlow<Boolean> = _expandLevels.asStateFlow()

    private val _isShowAllThemes: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isShowAllThemes: StateFlow<Boolean> = _isShowAllThemes.asStateFlow()

    val checkedAllDays: StateFlow<Boolean> by lazy {
        profileUserCase.settingsProfile.map { model ->
            model.allDays.toString().toBoolean()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, true)
    }

    val themes: StateFlow<List<ThemeModel>> by lazy {
        profileUserCase.themes.combine(profileUserCase.settingsProfile) { themes, profile ->
            themes.map { model ->
                model.copy(
                    isChecked = model.name == profile.theme
                )
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    private val _progress: MutableStateFlow<ProgressModel?> = MutableStateFlow(null)
    val progress: StateFlow<ProgressModel?> = _progress.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val stateUi: StateFlow<Result<ProgressModel?>> by lazy {
        level.flatMapLatest { level ->
            themes.flatMapLatest { themes ->
                var countSuccess = 0
                var countError = 0
                var countLearnt = 0
                try {
                    val theme = themes.first { theme ->
                        theme.isChecked
                    }
                    profileUserCase.words(
                        theme.name
                    ).map { snapshots ->
                        snapshots.children.map { snapshot ->
                            snapshot.getValue(WordModel::class.java)
                        }.forEach { model ->
                            countSuccess += model?.countSuccess ?: 0
                            countError += model?.countError ?: 0
                            if (model?.countSuccess == level.countSuccess)
                                countLearnt += 1
                        }
                        ProgressModel(
                            name = theme.name.replaceFirstChar { char ->
                                if (char.isLowerCase()) char.titlecase(
                                    Locale.ROOT
                                ) else char.toString()
                            }.trim(),
                            progress = theme.progress,
                            countSuccess = countSuccess,
                            countError = countError,
                            countLearnt = countLearnt,
                            size = snapshots.children.map { snapshot ->
                                snapshot.getValue(WordModel::class.java)
                            }.size
                        )
                    }
                } catch (e: Exception) {
                   flow {emit(ProgressModel()) }
                }
            }
        }.asResult().map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Success -> Result.Success(result.data)
                is Result.Error -> Result.Error(result.throwable)
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, Result.Loading)
    }


    val timeFrom: StateFlow<TimeModel> by lazy {
        profileUserCase.settingsProfile.map { profile ->
            TimeModel(
                id = profile.timeFrom.replaceAfter(":", "").trim(':').toInt(),
                time = profile.timeFrom,
                isCheck = true
            )
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, TimeModel())
    }


    val timeTo: StateFlow<TimeModel> by lazy {
        profileUserCase.settingsProfile.map { profile ->
            TimeModel(
                id = profile.timeTo.replaceAfter(":", "").trim(':').toInt(),
                time = profile.timeTo,
                isCheck = true
            )
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, TimeModel())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val listTimeFrom: StateFlow<List<TimeModel>> by lazy {
        timeTo.flatMapLatest { timeTo ->
            listTimes.combine(timeFrom) { listTimes, timeFrom ->
                listTimes.map { model ->
                    model.copy(
                        isCheck = timeFrom.id == model.id
                    )
                }.filter { model ->
                    model.id < timeTo.id
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, listTime)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val listTimeTo: StateFlow<List<TimeModel>> by lazy {
        timeFrom.flatMapLatest { timeFrom ->
            listTimes.combine(timeTo) { listTimes, timeTo ->
                listTimes.map { model ->
                    model.copy(
                        isCheck = timeTo.id == model.id
                    )
                }.filter { model ->
                    model.id > timeFrom.id
                }
                    .sortedByDescending { model ->
                        model.id
                    }
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, listTime)
    }

    val levels: StateFlow<List<LevelModel>> by lazy {
        _levels.combine(profileUserCase.settingsProfile) { levels, profile ->
            levels.map { model ->
                model.copy(
                    check = model.countSuccess.toString() == profile.countSuccess
                )
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, listLevels)
    }

    val level: StateFlow<LevelModel> by lazy {
        profileUserCase.settingsProfile.combine(_levels) { profile, listLevels ->
            try {
                listLevels.first { model ->
                    model.countSuccess.toString() == profile.countSuccess
                }
            } catch (e: Exception) {
                LevelModel()
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, LevelModel())
    }


    val isRemember: StateFlow<Boolean> by lazy {
        profileUserCase.settingsProfile.map { model ->
            model.remember.toString().toBoolean()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
    }

    val days: StateFlow<List<DayModel>> by lazy {
        profileUserCase.settingsProfile.map { profile ->
            profile.days.values.sortedBy { model ->
                model.id
            }.toList()
        }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun onCheckedChangeRemember(value: Boolean, unit: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.onCheckedChangeRemember(value = value)
            unit()
        }
    }

    fun onCheckedChangeAllDays(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.onCheckedChangeAllDays(value = value)
            if (value) profileUserCase.checkAllDays()
            else profileUserCase.unCheckAllDays()
        }
    }

    fun checkDay(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.checkDay(name = name)
        }
    }

    fun unCheckDay(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.unCheckDay(name = name)
        }
    }


    fun checkTheme(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.checkTheme(name = name)
        }
    }

    fun setCountThemes(count: Int, isShowAllThemes: Boolean) {
        viewModelScope.launch {
            _isShowAllThemes.emit(isShowAllThemes.not())
            _countThemes.emit(count)
        }
    }

    fun expandTimeFrom() {
        viewModelScope.launch {
            _expandTimeFrom.emit(true)
        }
    }

    fun hideTimeFrom() {
        viewModelScope.launch {
            _expandTimeFrom.emit(false)
        }
    }

    fun expandTimeTo() {
        viewModelScope.launch {
            _expandTimeTo.emit(true)
        }
    }

    fun hideTimeTo() {
        viewModelScope.launch {
            _expandTimeTo.emit(false)
        }
    }

    fun expandLevels() {
        viewModelScope.launch {
            _expandLevels.emit(true)
        }
    }

    fun hideLevels() {
        viewModelScope.launch {
            _expandLevels.emit(false)
        }
    }

    fun setTimeFrom(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.setTimeFrom(time = time)
        }
    }

    fun setTimeTo(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            profileUserCase.setTimeTo(time = time)
        }
    }

    fun setLevel(level: LevelModel?, currentLevel: LevelModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (level != null) {
                when {
                    level.id > currentLevel.id -> {
                        profileUserCase.setCount(count = level.countSuccess.toString())
                    }

                    level.id < currentLevel.id -> {
                        profileUserCase.setCount(count = level.countSuccess.toString())
                        profileUserCase.changeCountInWords(count = level.countSuccess.toString())
                    }

                    else -> {}
                }
            }
        }
    }

    fun setProgress(progress: ProgressModel) {
        viewModelScope.launch {
            _progress.emit(progress)
        }
    }
}