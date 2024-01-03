package com.gmail.remember.receiver

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.gmail.remember.R
import com.gmail.remember.domain.usercases.ProfileUserCase
import com.gmail.remember.domain.usercases.TrainingWordsUserCase
import com.gmail.remember.models.WordModel
import com.gmail.remember.models.isShowNotification
import com.gmail.remember.navigation.DEEP_LINK_TRAINING
import com.gmail.remember.screens.profile.ACTION_ANSWER
import com.gmail.remember.screens.profile.ACTION_CANCEL_ALARM
import com.gmail.remember.screens.profile.ACTION_START_ALARM
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


const val PUS_NOTIFICATION_CHANNEL = "REMEMBER"
const val KEY_QUICK_REPLY_TEXT = "KEY_QUICK_REPLY_TEXT"
const val NOTIFICATION_ID = 0
const val MILLS = 60 * 1000

@AndroidEntryPoint
class RememberBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var profileUserCase: ProfileUserCase

    @Inject
    lateinit var trainingWordsUserCase: TrainingWordsUserCase

    private var notificationManager: NotificationManager? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_START_ALARM -> {
                setNewAlarm(context = context) {
                    initNotificationManager(context = context, isAnswer = false)
                    if (intent.data != null)
                        showNotification(
                            context = context,
                            word = Gson().fromJson(
                                intent.data.toString(),
                                WordModel::class.java
                            )
                        )
                }
            }

            ACTION_ANSWER -> {
                initNotificationManager(context = context, isAnswer = true)
                if (intent.data != null) onAnswer(
                    intent = intent,
                    context = context,
                    word = Gson().fromJson(
                        intent.data.toString(),
                        WordModel::class.java
                    )
                )
            }

            ACTION_CANCEL_ALARM -> context?.getSystemService<AlarmManager>()?.cancel(
                PendingIntent.getBroadcast(
                    context,
                    NOTIFICATION_ID,
                    Intent(context, RememberBroadcastReceiver::class.java).apply {
                        action = ACTION_START_ALARM
                    },
                    FLAG_IMMUTABLE
                )
            )
        }
    }

    private fun initNotificationManager(context: Context?, isAnswer: Boolean) {
        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager?.getNotificationChannel(PUS_NOTIFICATION_CHANNEL) == null) {
            val channel = NotificationChannel(
                PUS_NOTIFICATION_CHANNEL,
                PUS_NOTIFICATION_CHANNEL,
                if (isAnswer) NotificationManager.IMPORTANCE_LOW else NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setNewAlarm(context: Context?, result: () -> Unit) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            profileUserCase.settingsProfile.flatMapLatest { profile ->
                if (profile.isShowNotification())
                    profileUserCase.words(profile.theme).map { data ->
                        data.children.map { snapshot ->
                            snapshot.getValue(WordModel::class.java)
                        }.filter { word ->
                            word?.countSuccess != profile.countSuccess.toInt()
                        }
                    }
                else emptyFlow()
            }.collectLatest { words ->
                profileUserCase.period.collectLatest { period ->
                    val word =
                        if (words.isNotEmpty() && words.size > 1) words[Random.nextInt(
                            0,
                            words.size
                        )]
                        else if (words.size == 1) words[0] else null
                    val alarmManager: AlarmManager? = context?.getSystemService()
                    val alarmPendingIntent = PendingIntent.getBroadcast(
                        context,
                        NOTIFICATION_ID,
                        Intent(context, RememberBroadcastReceiver::class.java).apply {
                            action = ACTION_START_ALARM
                            data = if (word != null) Uri.parse(Gson().toJson(word)) else null
                        },
                        FLAG_IMMUTABLE
                    )
                    alarmManager?.apply {
                        setExactAndAllowWhileIdle(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + period * MILLS,
                            alarmPendingIntent
                        )
                    }
                    result()
                    cancel()
                }
            }
        }
    }

    private fun showNotification(
        context: Context?,
        word: WordModel?
    ) {
        notificationManager?.notify(
            NOTIFICATION_ID,
            createNewNotification(
                context = context,
                word = word
            )
        )
    }

    private fun createNewNotification(
        context: Context?,
        word: WordModel?,
    ): Notification {
        val actionAnswer: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            context?.getString(R.string.answer),
            PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                Intent(context, RememberBroadcastReceiver::class.java).apply {
                    action = ACTION_ANSWER
                    data = Uri.parse(Gson().toJson(word))
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
            .setSilent(false)
            .setContentTitle(word?.wordEng)
            .setSmallIcon(R.drawable.ic_remember)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID,
                    Intent(
                        Intent.ACTION_VIEW,
                        "$DEEP_LINK_TRAINING/${word?.wordEng}".toUri()
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    },
                    PendingIntent.FLAG_MUTABLE
                )
            )
            .addAction(actionAnswer)
            .build()
    }

    private fun onAnswer(
        intent: Intent?,
        word: WordModel?,
        context: Context?
    ) {

        val result: String = intent?.let {
            RemoteInput.getResultsFromIntent(it)
                ?.getString(KEY_QUICK_REPLY_TEXT)
        } ?: ""

        if (word != null) {
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                if (result.uppercase().trim() == word.wordRu.uppercase().trim())
                    trainingWordsUserCase.changeCountSuccessInWord(wordModel = word)
                else trainingWordsUserCase.changeCountErrorInWord(wordModel = word)
            }
        }

        notificationManager?.notify(
            NOTIFICATION_ID,
            createNotificationResult(
                context = context,
                word = word,
                result = result
            )
        )
    }

    private fun createNotificationResult(
        word: WordModel?,
        context: Context?,
        result: String
    ): Notification {
        val actionMore: NotificationCompat.Action = NotificationCompat.Action.Builder(
            null,
            context?.getString(R.string.more),
            PendingIntent.getActivity(
                context!!,
                NOTIFICATION_ID,
                Intent(
                    Intent.ACTION_VIEW,
                    if (result.uppercase().trim() == word?.wordRu?.uppercase()
                            ?.trim()
                    ) "${DEEP_LINK_TRAINING}/${null}".toUri() else "$DEEP_LINK_TRAINING/${word?.wordEng ?: ""}".toUri()
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
                PendingIntent.FLAG_MUTABLE
            )
        )
            .build()
        return NotificationCompat.Builder(
            context,
            PUS_NOTIFICATION_CHANNEL
        )
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_remember)
            .setAutoCancel(true)
            .setContentTitle(
                if (result.uppercase().trim() == word?.wordRu?.uppercase()
                        ?.trim()
                ) context.getString(
                    R.string.right
                ) else context.getString(
                    R.string.wrong
                )
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID,
                    Intent(
                        Intent.ACTION_VIEW,
                        if (result.uppercase().trim() == word?.wordRu?.uppercase()
                                ?.trim()
                        ) "${DEEP_LINK_TRAINING}/${null}".toUri() else "$DEEP_LINK_TRAINING/${word?.wordEng ?: ""}".toUri()
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    },
                    PendingIntent.FLAG_MUTABLE
                )
            )
            .setContentText(
                if (result.uppercase().trim() == word?.wordRu?.uppercase()
                        ?.trim()
                ) "🍾🍾🍾🍾🍾🍾🍾🍾🍾🍾🍾🍾"
                else context.getString(
                    R.string.correct_answer,
                    word?.wordRu?.uppercase()
                )
            )
            .addAction(actionMore)
            .build()
    }
}