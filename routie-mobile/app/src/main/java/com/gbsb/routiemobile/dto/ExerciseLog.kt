package com.gbsb.routiemobile.dto

data class ExerciseLog(
    val id: Long,
    val exerciseName: String,
    val duration: Int,
    val caloriesBurned: Double
)
