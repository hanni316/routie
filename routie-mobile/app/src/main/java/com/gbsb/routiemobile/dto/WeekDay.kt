package com.gbsb.routiemobile.dto

import java.time.LocalDate

data class WeekDay(
    val date: LocalDate,
    val dayOfWeek: String,
    val isSelected: Boolean
)
