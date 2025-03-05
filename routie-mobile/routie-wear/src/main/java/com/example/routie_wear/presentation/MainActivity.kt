package com.example.routie_wear.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import android.content.SharedPreferences
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    // 걸음 수를 저장할 SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private var stepCount by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SharedPreferences 초기화 (걸음 수 저장용)
        sharedPreferences = getSharedPreferences("step_prefs", Context.MODE_PRIVATE)
        stepCount = sharedPreferences.getInt("step_count", 0)

        // 센서 매니저 설정
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            Log.d("SensorCheck", "걸음 센서 감지됨: ${stepSensor!!.name}")
            val registered = sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.d("SensorCheck", "센서 리스너 등록됨: $registered")
        } else {
            Log.e("SensorCheck", "걸음 센서를 찾을 수 없음!")
        }

        setContent {
            WearApp(stepCount)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            stepCount = event.values[0].toInt()

            // 걸음 수 업데이트 로그 추가
            Log.d("StepCounter", "걸음 감지됨! 현재 걸음 수: $stepCount")

            // 걸음 수를 SharedPreferences에 저장
            sharedPreferences.edit().putInt("step_count", stepCount).apply()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}

@Composable
fun WearApp(stepCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = "걸음 수: $stepCount"
        )
    }
}

@Preview
@Composable
fun DefaultPreview() {
    WearApp(stepCount = 10)
}
