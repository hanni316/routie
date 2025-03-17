package com.gbsb.routiemobile.dto

data class RoutineRequest(
    val name: String,
    val description: String,
    val exercises: List<ExerciseRequest> // 루틴에 포함된 운동 리스트
)
