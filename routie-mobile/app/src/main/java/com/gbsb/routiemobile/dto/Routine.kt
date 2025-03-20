package com.gbsb.routiemobile.dto

data class Routine(
    val id: Long,
    val name: String,
    val description: String,
    val exercises: List<String>
)
