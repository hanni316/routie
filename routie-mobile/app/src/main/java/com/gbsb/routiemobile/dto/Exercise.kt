package com.gbsb.routiemobile.dto

data class Exercise(
    val id: Long,
    val name: String,
    val caloriesPerSecond: Double,
    val categoryId: Int
)
