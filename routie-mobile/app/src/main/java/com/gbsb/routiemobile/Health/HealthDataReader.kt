package com.gbsb.routiemobile.health

import android.util.Log
import com.samsung.android.sdk.healthdata.*

class HealthDataReader(private val healthDataStore: HealthDataStore) {

    private val resolver = HealthDataResolver(healthDataStore, null)

    fun readStepsAndCalories(
        onResult: (steps: Int, calories: Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 * 60 * 60 * 24 // 최근 24시간 기준

        val stepRequest = HealthDataResolver.ReadRequest.Builder()
            .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
            .setProperties(arrayOf(HealthConstants.StepCount.COUNT))
            .setLocalTimeRange(
                HealthConstants.StepCount.START_TIME,
                HealthConstants.StepCount.END_TIME,
                startTime,
                endTime
            )
            .build()

        val calRequest = HealthDataResolver.ReadRequest.Builder()
            .setDataType(HealthConstants.Exercise.HEALTH_DATA_TYPE)
            .setProperties(arrayOf(HealthConstants.Exercise.CALORIE))
            .setLocalTimeRange(
                HealthConstants.Exercise.START_TIME,
                HealthConstants.Exercise.END_TIME,
                startTime,
                endTime
            )
            .build()

        try {
            resolver.read(stepRequest).setResultListener { stepResult ->
                var totalSteps = 0
                stepResult.forEach { totalSteps += it.getInt(HealthConstants.StepCount.COUNT) }

                resolver.read(calRequest).setResultListener { calResult ->
                    var totalCalories = 0.0
                    calResult.forEach { totalCalories += it.getDouble(HealthConstants.Exercise.CALORIE) }

                    onResult(totalSteps, totalCalories)
                }
            }
        } catch (e: Exception) {
            onError("헬스 데이터 읽기 실패: ${e.message}")
            Log.e("HealthDataReader", e.message ?: "Unknown error")
        }
    }
}