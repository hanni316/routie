package com.gbsb.routiemobile.dto
//로그인 응답 dto
data class LoginResponse(
    val userId: String,
    val name: String,
    val gold: Int,
    val totalCaloriesBurned: Double,
    val profileImageUrl: String?
)
