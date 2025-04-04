package com.gbsb.routiemobile.dto

data class RoutineUpdateRequest(
    val name: String,
    val description: String,
    val days: List<String>
)