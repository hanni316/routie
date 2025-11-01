package com.gbsb.routiemobile.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import java.time.ZonedDateTime

object AlarmScheduler {
    const val ACTION_MORNING = "com.routie.action.MORNING_REMINDER"
    const val ACTION_EVENING = "com.routie.action.EVENING_REMINDER"

    fun scheduleDailyAlarms(context: Context) {
        // 아침 8시
        scheduleExactDaily(context, 8, 0, ACTION_MORNING, 1001)
        // 저녁 8시
        scheduleExactDaily(context, 20, 0, ACTION_EVENING, 1002)
    }

    private fun scheduleExactDaily(
        context: Context,
        hour: Int,
        minute: Int,
        action: String,
        requestCode: Int
    ) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(action).setPackage(context.packageName)
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 오늘/내일 목표 시각 계산
        val now = ZonedDateTime.now()
        var next = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (next.isBefore(now)) {
            next = next.plusDays(1)
        }
        val triggerAt = next.toInstant().toEpochMilli()

        // 테스트 용도
//        val now = System.currentTimeMillis()
//        val triggerAt = now + 10_000 // 10초 뒤

        try {
            // Android 12(S)+ 에서는 정확 알람 가능한지 먼저 본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val canExact = am.canScheduleExactAlarms()
                if (canExact) {
                    setExactCompat(am, triggerAt, pi)
                } else {
                    am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
                }
            } else {
                // S 미만은 기존대로
                setExactCompat(am, triggerAt, pi)
            }
        } catch (e: SecurityException) {
            // 권한이 진짜로 없을 때
            am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        }
    }

    // 정확 알람 호출 부분만 분리
    private fun setExactCompat(
        am: AlarmManager,
        triggerAtMillis: Long,
        pi: PendingIntent
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        }
    }

    @Suppress("unused")
    private fun openExactAlarmSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = android.net.Uri.parse("package:${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    fun rescheduleNextDay(context: Context, action: String, requestCode: Int) {
        val hour = if (action == ACTION_MORNING) 8 else 20
        scheduleExactDaily(context, hour, 0, action, requestCode)
    }
}
