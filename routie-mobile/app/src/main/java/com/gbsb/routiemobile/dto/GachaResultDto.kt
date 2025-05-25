package com.gbsb.routiemobile.dto

data class GachaResultDto (
    val userId: String,
    val itemId: Long?,
    val isSuccess: Boolean,
    val isHiddenItem: Boolean
)