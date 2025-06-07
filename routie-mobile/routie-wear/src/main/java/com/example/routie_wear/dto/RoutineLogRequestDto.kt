package com.example.routie_wear.dto

data class RoutineLogRequestDto(
    val routineId: Long,
    val userId: String,
    val exercises: List<ExerciseLogRequestDto>
)