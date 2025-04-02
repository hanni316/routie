package com.example.routie_wear.dto

data class WorkoutRecordDto(
    val routineId: Long,
    val workoutName : String,
    val durationSeconds: Int
)