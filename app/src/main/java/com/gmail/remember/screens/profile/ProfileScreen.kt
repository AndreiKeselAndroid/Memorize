package com.gmail.remember.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gmail.remember.R
import com.gmail.remember.common.components.CheckBox
import com.gmail.remember.common.components.RadioButton
import com.gmail.remember.common.components.Switch
import com.gmail.remember.ui.theme.BlackBrown
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import java.util.Locale

const val ACTION_START_ALARM = "ACTION_START_ALARM"

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
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .background(color = GraphiteBlack),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = GraphiteBlack)
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 24.dp,
                            bottomStart = 24.dp
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
                        Text(
                            text = stringResource(R.string.settings),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                    .background(color = BlackBrown.copy(alpha = 0.5f))
            ) {

                Switch(
                    checked = isRemember,
                    text = R.string.on_notifications,
                    onCheckedChange = { value ->
                        viewModel.onCheckedChangeRemember(value) {
//                        val alarmPendingIntent = PendingIntent.getBroadcast(
//                            context,
//                            0,
//                            Intent(context, RememberBroadcastReceiver::class.java).apply {
//                                action = ACTION_START_ALARM
//                            },
//                            FLAG_IMMUTABLE
//                        )
//                        if (value) {
//                            context.getSystemService<AlarmManager>()?.apply {
//                                setExactAndAllowWhileIdle(
//                                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                                    SystemClock.elapsedRealtime() + 10 * 1000,
//                                    alarmPendingIntent
//                                )
//                            }
//                        } else {
//                            context.getSystemService<AlarmManager>()?.apply {
//                                cancel(alarmPendingIntent)
//                            }
//                        }
                        }
                    }
                )

                if (isRemember) Divider(color = BlackBrown)
            }

            AnimatedVisibility(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                visible = isRemember
            ) {
                Column(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomStart = if (themes.isEmpty()) 24.dp else 0.dp,
                                bottomEnd = if (themes.isEmpty()) 24.dp else 0.dp
                            )
                        )
                        .background(color = BlackBrown.copy(alpha = 0.5f))
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
                                .background(color = BlackBrown.copy(alpha = 0.5f))
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
                        color = BlackBrown
                    )

                    if (themes.isNotEmpty()) Column(
                        modifier = Modifier
                            .background(GraphiteBlack)
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                            .background(color = BlackBrown.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp)
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
}