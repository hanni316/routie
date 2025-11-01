package com.gbsb.routiemobile.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MorningAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        NotificationHelper.show(
            context,
            id = 800,
            title = "굿모닝! 루틴 시작할 시간 👟",
            message = "오늘 루틴 한 번 확인해요!"
        )

        // 내일 아침도 다시 예약
        AlarmScheduler.rescheduleNextDay(
            context,
            AlarmScheduler.ACTION_MORNING,
            1001
        )
    }
}
