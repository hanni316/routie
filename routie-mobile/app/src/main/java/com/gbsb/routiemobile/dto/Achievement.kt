package com.gbsb.routiemobile.dto

data class Achievement(
    val id: Long,
    val title: String,
    val description: String,
    val code: String,
    val category: String,
    val threshold: Int,
    val achieved: Boolean
)
