package com.gbsb.routiemobile.dto

data class Routine(
    val name: String,
    val description: String,
    val duration: Int, // 운동 시간 (분)
    val caloriesBurned: Int // 소모 칼로리
)
