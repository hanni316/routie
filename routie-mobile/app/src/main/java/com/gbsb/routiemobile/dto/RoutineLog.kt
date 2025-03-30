package com.gbsb.routiemobile.dto

data class RoutineLog(
    val totalCaloriesBurned: Double,
    val totalDuration: Int,
    val exerciseLogs: List<ExerciseLog>
)
