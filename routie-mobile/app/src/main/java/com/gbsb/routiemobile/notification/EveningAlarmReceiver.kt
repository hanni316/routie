package com.gbsb.routiemobile.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class EveningAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        NotificationHelper.show(
            context,
            id = 2000,
            title = "오늘 루틴 마무리 했나요?",
            message = "아직 기록 안 했으면 지금 체크해요 📝"
        )

        // 내일 저녁 다시 예약
        AlarmScheduler.rescheduleNextDay(
            context,
            AlarmScheduler.ACTION_EVENING,
            1002
        )
    }
}