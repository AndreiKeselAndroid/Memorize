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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
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
import com.gmail.remember.ui.theme.GraphiteBlack
import com.gmail.remember.ui.theme.GrayishOrange
import com.gmail.remember.ui.theme.UmberGray
import com.gmail.remember.ui.theme.White
import java.util.Locale

const val ACTION_START_ALARM = "ACTION_START_ALARM"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val displayName by viewModel.displayName.collectAsState()
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
            Column {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TopAppBarDefaults
                        .centerAlignedTopAppBarColors(
                            containerColor = GraphiteBlack,
                            titleContentColor = GrayishOrange
                        ),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayName,
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
                Divider(
                    thickness = 0.5.dp,
                    color = UmberGray
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = GraphiteBlack)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(color = GraphiteBlack)
            )

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

            Divider(
                modifier = Modifier.padding(16.dp),
                color = UmberGray
            )

            AnimatedVisibility(visible = isRemember) {
                Column(
                    modifier = Modifier
                        .background(color = GraphiteBlack)
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

                    AnimatedVisibility(visible = !checkedAllDays) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .background(color = GraphiteBlack)
                        ) {
                            days.forEach { model ->
                                CheckBox(
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
                    Divider(
                        modifier = Modifier.padding(16.dp),
                        color = UmberGray
                    )

                    if (themes.isNotEmpty()) Column(
                        modifier = Modifier
                            .background(color = GraphiteBlack)
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
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            viewModel.setCountThemes(
                                                count = if (isShowAllThemes) DEFAULT_COUNT_THEMES else themes.size,
                                                isShowAllThemes = isShowAllThemes
                                            )
                                        },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(color = GraphiteBlack.copy(alpha = 0.32f))
                                    )
                                    .padding(vertical = 8.dp),
                                text = if (isShowAllThemes) stringResource(R.string.hide) else stringResource(
                                    R.string.all
                                ),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.End,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                color = White
                            )
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = UmberGray
                        )
                    }
                }
            }
        }
    }
}