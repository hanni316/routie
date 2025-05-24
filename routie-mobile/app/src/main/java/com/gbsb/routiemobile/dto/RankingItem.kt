package com.gbsb.routiemobile.dto

data class RankingItem(
    val rank: Int,
    val userId: String,
    val nickname: String,
    val profileImageUrl: String?,
    val value: Double
)
