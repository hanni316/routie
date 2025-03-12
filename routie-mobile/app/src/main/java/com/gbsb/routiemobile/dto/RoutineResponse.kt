package com.gbsb.routiemobile.dto

data class RoutineResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val totalCaloriesBurned: Double
)
