package com.gmail.remember.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gmail.remember.R
import com.gmail.remember.common.components.RadioButton
import com.gmail.remember.common.components.Switch
import com.gmail.remember.ui.theme.GrayBlack

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
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White
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
                            tint = Color.White,
                            contentDescription = "BackIcon"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = Color.Black)
                .fillMaxSize()
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

            Divider(
                modifier = Modifier.padding(16.dp),
                color = GrayBlack
            )

            AnimatedVisibility(visible = isRemember) {
                Column(
                    modifier = Modifier
                        .background(color = Color.Black)
                        .fillMaxSize()
                ) {
                    Switch(
                        checked = checkedAllDays,
                        text = R.string.all_days,
                        onCheckedChange = { value ->
                            viewModel.onCheckedChangeAllDays(value = value)
                        }
                    )

                    AnimatedVisibility(visible = !checkedAllDays) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .background(color = Color.Black)
                        ) {
                            days.forEach { model ->
                                RadioButton(
                                    checked = model.check,
                                    onCheckedChange = {value->
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
                        color = GrayBlack
                    )
                }
            }
        }
    }
}