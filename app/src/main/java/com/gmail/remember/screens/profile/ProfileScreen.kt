package com.gmail.remember.screens.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.gmail.remember.R
import com.gmail.remember.common.components.CheckBox
import com.gmail.remember.common.components.OutlineTextField
import com.gmail.remember.common.components.RadioButton
import com.gmail.remember.common.components.Switch
import com.gmail.remember.data.api.models.Result
import com.gmail.remember.models.LevelModel
import com.gmail.remember.models.ProgressModel
import com.gmail.remember.models.TimeModel
import com.gmail.remember.models.isShowProgress
import com.gmail.remember.receiver.RememberBroadcastReceiver
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import com.gmail.remember.ui.theme.Green
import java.util.Locale

const val ACTION_START_ALARM = "ACTION_START_ALARM"
const val ACTION_CANCEL_ALARM = "ACTION_CANCEL_ALARM"
const val ACTION_ANSWER = "ACTION_ANSWER"

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isRemember by viewModel.isRemember.collectAsState()
    val checkedAllDays by viewModel.checkedAllDays.collectAsState()
    val days by viewModel.days.collectAsState()
    val themes by viewModel.themes.collectAsState()
    val countThemes by viewModel.countThemes.collectAsState()
    val isShowAllThemes by viewModel.isShowAllThemes.collectAsState()
    val timeFrom by viewModel.timeFrom.collectAsState()
    val timeTo by viewModel.timeTo.collectAsState()
    val expandTimeFrom by viewModel.expandTimeFrom.collectAsState()
    val expandTimeTo by viewModel.expandTimeTo.collectAsState()
    val listTimeFrom by viewModel.listTimeFrom.collectAsState()
    val listTimeTo by viewModel.listTimeTo.collectAsState()
    val levels by viewModel.levels.collectAsState()
    val currentLevel by viewModel.level.collectAsState()
    val expandLevels by viewModel.expandLevels.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val stateUi by viewModel.stateUi.collectAsState()
    val settingsProfile by viewModel.settingsProfile.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading_lottie)
    )
    val progressLottie by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )

    Scaffold(
        modifier = Modifier
            .background(color = GraphiteBlack),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = if (progress.isShowProgress(isRemember = isRemember)) 0.dp
                            else if (scrollState.value != 0) 0.dp else 24.dp,
                            bottomStart = if (progress.isShowProgress(isRemember = isRemember)) 0.dp
                            else if (scrollState.value != 0) 0.dp else 24.dp
                        )
                    )
                    .fillMaxWidth(),
                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(
                        containerColor = BlackBrown,
                        titleContentColor = GrayishOrange
                    ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = stringResource(
                                    R.string.profile
                                ),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (progress?.name?.isNotEmpty() == true &&
                                progress.isShowProgress(isRemember = isRemember)
                            ) Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 4.dp
                                    ),
                                text = stringResource(
                                    R.string.active_theme,
                                    progress?.name ?: ""
                                ),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                color = GrayishOrange
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = GrayishOrange,
                            contentDescription = "BackIcon"
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(40.0.dp))
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = GraphiteBlack)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (progress.isShowProgress(isRemember = isRemember)) {
                Row(
                    modifier = Modifier
                        .padding(top = 0.5.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = 24.dp,
                                bottomStart = 24.dp
                            )
                        )
                        .background(color = BlackBrown)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp
                                ),
                            text = stringResource(
                                R.string.all_words,
                                progress?.size.toString()
                            ),
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            color = GrayishOrange
                        )

                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp
                                ),
                            text = stringResource(
                                R.string.learn_words,
                                progress?.countLearnt.toString()
                            ),
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            color = GrayishOrange
                        )

                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp
                                ),
                            text = stringResource(
                                R.string.answer_success,
                                progress?.countSuccess.toString()
                            ),
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            color = GrayishOrange
                        )

                        Text(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 16.dp
                                ),
                            text = stringResource(
                                R.string.answer_error,
                                progress?.countError.toString()
                            ),
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            color = GrayishOrange
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .size(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${((progress?.progress ?: 0f) * 100).toInt()}%",
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            color = GrayishOrange
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Red.copy(alpha = 0.32f),
                            progress = 1f
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            color = Green,
                            progress = progress?.progress ?: 0f
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 4.dp, end = 4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = if (!isRemember) 24.dp else 0.dp,
                            bottomEnd = if (!isRemember) 24.dp else 0.dp
                        )
                    )
                    .background(color = BlackBrown.copy(alpha = 0.22f))
            ) {

                Switch(
                    checked = isRemember,
                    text = R.string.on_notifications,
                    onCheckedChange = { value ->
                        viewModel.onCheckedChangeRemember(value) {
                            if (value) context.sendBroadcast(
                                    Intent(
                                        context,
                                        RememberBroadcastReceiver::class.java
                                    ).apply {
                                        action = ACTION_START_ALARM
                                    })
                            else context.sendBroadcast(
                                    Intent(
                                        context,
                                        RememberBroadcastReceiver::class.java
                                    ).apply {
                                        action = ACTION_CANCEL_ALARM
                                    })
                        }
                    }
                )

                if (isRemember && settingsProfile.theme.isEmpty())
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 12.dp
                            ),
                        text = stringResource(R.string.chose_theme),
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        lineHeight = 24.sp,
                        color = Color.Yellow
                    )

                if (isRemember) Divider(
                    modifier = Modifier.padding(bottom = 12.dp),
                    thickness = 3.dp,
                    color = GraphiteBlack
                )
            }

            AnimatedVisibility(
                visible = isRemember
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .fillMaxWidth()
                        .background(color = BlackBrown.copy(alpha = 0.22f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(16.dp),
                            text = stringResource(id = R.string.level),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            color = GrayishOrange
                        )

                        CompositionLocalProvider(
                            LocalTextInputService provides null
                        ) {
                            OutlineTextField(
                                list = levels,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f))
                                    ) {
                                        viewModel.expandLevels()
                                    }
                                    .padding(horizontal = 12.dp)
                                    .weight(0.5f),
                                onDismissDropdownMenu = {
                                    viewModel.hideLevels()
                                },
                                enabled = false,
                                expandTime = mutableStateOf(expandLevels),
                                onClickItemMenu = { level ->
                                    viewModel.setLevel(
                                        level = level as? LevelModel,
                                        currentLevel = currentLevel
                                    )
                                    viewModel.hideLevels()
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(
                                            if (expandTimeFrom) R.drawable.ic_expand
                                            else R.drawable.ic_not_expand
                                        ),
                                        contentDescription = "Calendar",
                                        tint = GrayishOrange.copy(alpha = 0.32f)
                                    )
                                },
                                value = currentLevel.name
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(top = 20.dp),
                        thickness = 3.dp,
                        color = GraphiteBlack
                    )
                }
            }

            AnimatedVisibility(
                visible = isRemember
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .background(color = BlackBrown.copy(alpha = 0.22f))
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.time),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        color = GrayishOrange
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CompositionLocalProvider(
                            LocalTextInputService provides null
                        ) {
                            OutlineTextField(
                                list = listTimeFrom,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f))
                                    ) {
                                        viewModel.expandTimeFrom()
                                    }
                                    .padding(horizontal = 12.dp)
                                    .weight(1f),
                                onDismissDropdownMenu = {
                                    viewModel.hideTimeFrom()
                                },
                                enabled = false,
                                expandTime = mutableStateOf(expandTimeFrom),
                                onClickItemMenu = { time ->
                                    viewModel.setTimeFrom(time = (time as? TimeModel)?.id.toString())
                                    viewModel.hideTimeFrom()
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(
                                            if (expandTimeFrom) R.drawable.ic_expand
                                            else R.drawable.ic_not_expand
                                        ),
                                        contentDescription = "Calendar",
                                        tint = GrayishOrange.copy(alpha = 0.32f)
                                    )
                                },
                                value = timeFrom.time
                            )
                        }

                        CompositionLocalProvider(
                            LocalTextInputService provides null
                        ) {
                            OutlineTextField(
                                list = listTimeTo,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f))
                                    ) {
                                        viewModel.expandTimeTo()
                                    }
                                    .padding(horizontal = 12.dp)
                                    .weight(1f),
                                onDismissDropdownMenu = {
                                    viewModel.hideTimeTo()
                                },
                                enabled = false,
                                expandTime = mutableStateOf(expandTimeTo),
                                onClickItemMenu = { time ->
                                    viewModel.setTimeTo(time = (time as? TimeModel)?.id.toString())
                                    viewModel.hideTimeTo()
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(
                                            if (expandTimeTo) R.drawable.ic_expand
                                            else R.drawable.ic_not_expand
                                        ),
                                        contentDescription = "Calendar",
                                        tint = GrayishOrange.copy(alpha = 0.32f),
                                    )
                                },
                                value = timeTo.time,
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(top = 24.dp),
                        thickness = 3.dp,
                        color = GraphiteBlack
                    )
                }
            }

            AnimatedVisibility(
                visible = isRemember
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = if (themes.isEmpty()) 24.dp else 0.dp,
                                bottomEnd = if (themes.isEmpty()) 24.dp else 0.dp
                            )
                        )
                        .background(color = BlackBrown.copy(alpha = 0.22f))
                        .fillMaxSize()
                ) {
                    Switch(
                        checked = checkedAllDays,
                        text = R.string.all_days,
                        onCheckedChange = { value ->
                            viewModel.onCheckedChangeAllDays(value = value)
                        },
                        textColor = GrayishOrange
                    )

                    AnimatedVisibility(
                        visible = !checkedAllDays
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .background(color = GraphiteBlack)
                                .background(color = BlackBrown.copy(alpha = 0.22f))
                        ) {
                            days.forEachIndexed { index, model ->
                                CheckBox(
                                    modifier = Modifier.padding(bottom = if (index == 6 && themes.isEmpty()) 10.dp else 0.dp),
                                    checked = model.check,
                                    onCheckedChange = { value ->
                                        if (value) viewModel.checkDay(name = model.name)
                                        else viewModel.unCheckDay(name = model.name)
                                    },
                                    text = model.name
                                )
                            }
                        }
                    }

                    if (themes.isNotEmpty()) Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 3.dp,
                        color = GraphiteBlack
                    )

                    if (themes.isNotEmpty()) Column(
                        modifier = Modifier
                            .background(GraphiteBlack)
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                            .background(color = BlackBrown.copy(alpha = 0.22f))
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(id = R.string.themes),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            color = GrayishOrange
                        )

                        themes.forEachIndexed { index, model ->
                            if (index <= countThemes) RadioButton(
                                modifier = Modifier.padding(bottom = if (themes.size in 1..2) 10.dp else 0.dp),
                                checked = model.isChecked,
                                onCheckedChange = {
                                    viewModel.checkTheme(model.name)
                                },
                                text = model.name.replaceFirstChar { char ->
                                    if (char.isLowerCase()) char.titlecase(
                                        Locale.ROOT
                                    ) else char.toString()
                                }
                            )
                        }

                        if (themes.size > DEFAULT_COUNT_THEMES.plus(1)) {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            viewModel.setCountThemes(
                                                count = if (isShowAllThemes) DEFAULT_COUNT_THEMES else themes.size,
                                                isShowAllThemes = isShowAllThemes
                                            )
                                        },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ),
                                text = if (isShowAllThemes) stringResource(R.string.hide) else stringResource(
                                    R.string.more
                                ),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.End,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                color = GrayishOrange
                            )
                        }
                    }
                }
            }
        }
    }

    when (stateUi) {
        is Result.Success -> {
            viewModel.setProgress(
                progress = ((stateUi as? Result.Success<*>)?.data as? ProgressModel)
                    ?: ProgressModel()
            )
        }

        is Result.Loading -> {
            if (isRemember) Box(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .padding(20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition,
                    progressLottie,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        else -> {}
    }
}