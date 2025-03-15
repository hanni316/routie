package com.example.routie_wear.presentation

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

class MainFragment : Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var stepCount by mutableStateOf(0)

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        return ComposeView(requireContext()).apply {
            setContent {
                WearNavigation()  // ✅ Wear Navigation을 추가!
            }
        }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("step_prefs", Context.MODE_PRIVATE)
        stepCount = sharedPreferences.getInt("step_count", 0)

        // ✅ 센서 매니저 초기화
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            Log.d("SensorCheck", "걸음 센서 감지됨: ${stepSensor!!.name}")
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Log.e("SensorCheck", "걸음 센서를 찾을 수 없음!")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            stepCount = event.values[0].toInt()
            sharedPreferences.edit().putInt("step_count", stepCount).apply()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this) // ✅ 센서 리스너 해제 (리소스 절약)
    }
}

@Composable
fun WearNavigation() {
    val navController = rememberSwipeDismissableNavController()  // ✅ Wear OS 전용 네비게이션 컨트롤러
    WearApp()
}

@Composable
fun WearApp() {
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
            text = "걸음 수 측정 중..."
        )
    }
}