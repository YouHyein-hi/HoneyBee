package com.example.receiptcareapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.receiptcareapp.R
import com.example.receiptcareapp.ui.activity.MainActivity
import com.example.receiptcareapp.ui.fragment.MenuFragment
import java.util.*

class PushReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "MyPushChannel"

    override fun onReceive(context: Context, intent: Intent) {
        val requestCode = intent.getIntExtra("code", -1)
        if (requestCode == MenuFragment.REQUEST_CODE) {

            createNotificationChannel(context)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                UUID.randomUUID().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val contents = "청구되지않은 카드가 존재합니다."

            val builder01 = NotificationCompat.Builder(context, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.icon_add_camera)
                setContentTitle("HoneyBee")
                setContentText(contents)
                priority = NotificationCompat.PRIORITY_DEFAULT
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

            with(NotificationManagerCompat.from(context)) {
                notify(UUID.randomUUID().hashCode(), builder01.build())
            }
        }
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification_Ch"
            val descriptionText = "Test Notification"
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}