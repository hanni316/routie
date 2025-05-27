package com.gbsb.routiemobile.dto

data class GachaResultDto (
    val userId: String,
    val itemId: Long?,
    val isHiddenItem: Boolean,
    val isSuccess: Boolean
)