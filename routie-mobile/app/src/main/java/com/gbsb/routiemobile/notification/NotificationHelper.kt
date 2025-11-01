package com.gbsb.routiemobile.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.gbsb.routiemobile.MainActivity
import com.gbsb.routiemobile.R

object NotificationHelper {
    const val CHANNEL_ID = "routie_daily_reminders"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Routie 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "아침/저녁 루틴 알림"
            }

            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    fun show(context: Context, id: Int, title: String, message: String) {
        ensureChannel(context)

        // 안드로이드 13+ 에서는 알림 권한 없으면 그냥 리턴
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                // 권한 없으면 알림 안 띄우고 끝
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.logo_icon)

        val noti = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.system_icon) // 시스템 알림창 아이콘
            .setLargeIcon(largeIcon) // 알림바 내렸을 때 아이콘
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(id, noti)
        } catch (e: SecurityException) {
            // 권한 없을 때 조용히 무시
        }
    }
}
