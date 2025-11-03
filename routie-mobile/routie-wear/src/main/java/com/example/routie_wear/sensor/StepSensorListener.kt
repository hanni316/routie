package com.example.routie_wear.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.routie_wear.dto.StepGoalDto
import com.example.routie_wear.network.RetrofitInstance
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepSensorListener(private val context: Context, private val userId: String) : SensorEventListener {

    private var lastSentstepMilestone = 0  //보상 중복 방지용
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

            //최신 걸음수 캐시에 저장
            StepCache.lastKnownTotalSteps = stepCount

            val caloriesBurned = stepCount * 0.04f

            // milestone 기준
            val milestones = listOf(2000, 5000, 10000)
            for (milestone in milestones) {
                if (stepCount >= milestone && lastSentstepMilestone < milestone) {
                    lastSentstepMilestone = milestone
                    sendStepCountToServer(stepCount)
                    break
                }
            }

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

    private fun sendStepCountToServer(stepCount: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dto = StepGoalDto(stepCount)
                val response = RetrofitInstance.rewardApi.rewardSteps(userId, dto)
                if (response.isSuccessful) {
                    Log.d("StepSensor", "보상 서버 전송 성공! 걸음 수: $stepCount")
                } else {
                    Log.e("StepSensor", "서버 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("StepSensor", "서버 전송 오류: ${e.message}")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

