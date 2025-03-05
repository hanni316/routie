package com.example.routie_wear.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class StepSensorListener(context: Context) : SensorEventListener {
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private var dataClient: DataClient = Wearable.getDataClient(context)

    init {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val stepCount = event.values[0].toInt()
            val caloriesBurned = stepCount * 0.04f

            // 데이터 패스를 통해 스마트폰으로 전송
            val putDataMapReq = PutDataMapRequest.create("/calories")
            val dataMap = putDataMapReq.dataMap
            dataMap.putInt("step_count", stepCount)
            dataMap.putFloat("calories", caloriesBurned)

            val putDataReq = putDataMapReq.asPutDataRequest().setUrgent()
            dataClient.putDataItem(putDataReq)

            Log.d("WearOS", "전송 완료! Calories: $caloriesBurned kcal, Steps: $stepCount")

            Log.d("StepSensor", "걸음 수: $stepCount, 칼로리: $caloriesBurned kcal") // 디버그 로그 추가
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

