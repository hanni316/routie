package com.gbsb.routiemobile.health

import android.content.Context
import android.util.Log
import com.samsung.android.sdk.healthdata.*

class SamsungHealthManager(
    private val context: Context
) {
    private var store: HealthDataStore? = null

    fun fetchStepsAndCalories(
        onResult: (steps: Int, calories: Double) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            store = HealthDataStore(context, object : HealthDataStore.ConnectionListener {
                override fun onConnected() {
                    Log.d("SamsungHealth", "Connected to Samsung Health")
                    store?.let {
                        HealthDataReader(it).readStepsAndCalories(
                            onResult = onResult,
                            onError = onError
                        )
                    }
                }

                override fun onConnectionFailed(error: HealthConnectionErrorResult) {
                    onError("Samsung Health 연결 실패: ${error.errorCode}")
                }

                override fun onDisconnected() {
                    Log.w("SamsungHealth", "Samsung Health 연결 끊김")
                }
            })

            store?.connectService()
        } catch (e: Exception) {
            onError("Samsung Health 초기화 실패: ${e.message}")
        }
    }

    fun release() {
        store?.disconnectService()
    }
}