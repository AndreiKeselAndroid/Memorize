package com.gmail.remember.receiver

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.getSystemService
import com.gmail.remember.R
import com.gmail.remember.domain.usercases.ProfileUserCase
import com.gmail.remember.models.WordModel
import com.gmail.remember.screens.profile.ACTION_START_ALARM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random


const val PUS_NOTIFICATION_CHANNEL = "REMEMBER"
const val KEY_QUICK_REPLY_TEXT = "KEY_QUICK_REPLY_TEXT"
const val ACTION_ANSWER = "ACTION_ANSWER"
const val ACTION_MORE = "ACTION_MORE"

@AndroidEntryPoint
class RememberBroadcastReceiver : BroadcastReceiver(), CoroutineScope {

    @Inject
    lateinit var profileViewModel: ProfileUserCase

    private var notificationManager: NotificationManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        initNotificationManager(context = context)
        when (intent?.action) {
            ACTION_START_ALARM -> prepareNotificationAndNewAlarm(
                context = context,
                intent = intent
            )

            ACTION_ANSWER -> onAnswer(intent = intent, context = context)
            ACTION_MORE -> prepareMoreNotification(context = context, intent = intent)
        }
    }

    private fun prepareMoreNotification(
        context: Context?,
        intent: Intent?
    ) {
        launch {
            profileViewModel.words("words").collectLatest { snapshot ->
                val word: WordModel? = snapshot.children.mapIndexed { index, data ->
                    data.getValue(WordModel::class.java)?.copy(id = index)
                }[Random.nextInt(
                    0,
                    snapshot.children.shuffled().size.minus(1)
                )]
                notificationManager?.notify(
                    intent?.data.toString().replaceBefore('/', "").trim('/').toInt(),
                    createNewNotification(
                        context = context,
                        intent = intent,
                        word = word
                    )
                )
                coroutineContext.cancel()
            }
        }
    }

    private fun prepareNotificationAndNewAlarm(
        context: Context?,
        intent: Intent?
    ) {
        launch {
            profileViewModel.words("words").collectLatest { snapshot ->
                val word: WordModel? = snapshot.children.mapIndexed { index, data ->
                    data.getValue(WordModel::class.java)?.copy(id = index)
                }[Random.nextInt(
                    0,
                    snapshot.children.shuffled().size.minus(1)
                )]

                val alarmManager: AlarmManager? = context?.getSystemService()
                val alarmPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent ?: Intent(context, RememberBroadcastReceiver::class.java).apply {
                        action = ACTION_START_ALARM
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager?.apply {
                    setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 60 * 60 * 1000,
                        alarmPendingIntent
                    )
                }

                notificationManager?.notify(
                    word?.id ?: -1,
                    createNewNotification(
                        context = context,
                        intent = intent,
                        word = word
                    )
                )
                coroutineContext.cancel()
            }
        }
    }

    private fun onAnswer(
        intent: Intent?,
        context: Context?
    ) {
        launch {
            val answer: String = intent?.let { intent ->
                RemoteInput.getResultsFromIntent(intent)
                    ?.getString(KEY_QUICK_REPLY_TEXT)
            } ?: ""

            profileViewModel.words("words").collectLatest { snapshot ->
                val word: WordModel? = snapshot.children.mapIndexed { index, data ->
                    data.getValue(WordModel::class.java)?.copy(id = index)
                }[intent?.data.toString().replaceBefore('/', "").trim('/').toInt()]

                notificationManager?.notify(
                    intent?.data.toString().replaceAfter('/', "").trim('/').toInt(),
                    createNotificationResult(
                        word,
                        context,
                        intent = intent,
                        (if (word?.wordRu?.uppercase() == answer.uppercase()) context?.getString(R.string.right) else context?.getString(
                            R.string.wrong
                        )) ?: ""
                    )
                )
            }
            coroutineContext.cancel()
        }
    }

    private fun initNotificationManager(context: Context?) {
        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager?.getNotificationChannel(PUS_NOTIFICATION_CHANNEL) == null) {
            val channel = NotificationChannel(
                PUS_NOTIFICATION_CHANNEL,
                PUS_NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNewNotification(
        context: Context?,
        word: WordModel?,
        intent: Intent?
    ): Notification {
        val actionAnswer: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            context?.getString(R.string.answer),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, RememberBroadcastReceiver::class.java).apply {
                    action = ACTION_ANSWER
                    data = Uri.parse("${intent?.data?.toString() ?: word?.id}/${word?.id}")
                },
                PendingIntent.FLAG_MUTABLE
            )
        )
            .addRemoteInput(
                RemoteInput.Builder(KEY_QUICK_REPLY_TEXT)
                    .setLabel(context?.getString(R.string.enter_answer))
                    .build()
            )
            .build()
        return NotificationCompat.Builder(
            context!!,
            PUS_NOTIFICATION_CHANNEL
        )
            .setColor(Color.Black.toArgb())
            .setSmallIcon(R.drawable.ic_remember)
            .setAutoCancel(false)
            .setVibrate(LongArray(10))
            .setContentTitle(word?.wordEng)
            .addAction(actionAnswer)
            .build()
    }

    private fun createNotificationResult(
        word: WordModel?,
        context: Context?,
        intent: Intent?,
        result: String
    ): Notification {
        val actionMore: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            context?.getString(R.string.more),
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, RememberBroadcastReceiver::class.java).apply {
                    action = ACTION_MORE
                    data = Uri.parse(
                        "${
                            intent?.data?.toString()?.replaceBefore('/', "")?.trim('/')
                                ?.toInt() ?: word?.id
                        }/${word?.id}"
                    )
                },
                PendingIntent.FLAG_MUTABLE
            )
        )
            .build()
        return NotificationCompat.Builder(
            context!!,
            PUS_NOTIFICATION_CHANNEL
        )
            .setColor(Color.Black.toArgb())
            .setSmallIcon(R.drawable.ic_remember)
            .setContentTitle(word?.wordEng)
            .setContentText(
                context.getString(
                    R.string.correct_answer,
                    result.uppercase(),
                    word?.wordRu?.uppercase()
                )
            )
            .addAction(actionMore)
            .build()
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO
}