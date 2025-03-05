package com.example.routie_wear.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.routie_wear.sensor.StepSensorListener

class StepSensorService : Service() {
    private lateinit var stepSensorListener: StepSensorListener

    override fun onCreate() {
        super.onCreate()
        stepSensorListener = StepSensorListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}