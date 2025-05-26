package com.example.routie_wear.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.routie_wear.sensor.StepSensorListener

class StepSensorService : Service() {
    private lateinit var stepSensorListener: StepSensorListener

    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        if (userId != null) {
            Log.d("StepSensorService", "아이디 불러오기 성공: $userId")
            stepSensorListener = StepSensorListener(this, userId)
        } else {
            Log.e("StepSensorService", "아이디 없음. 앱에서 전달 안 됐을 수 있음.")
            stopSelf()  // 또는 대기/알림 처리
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}