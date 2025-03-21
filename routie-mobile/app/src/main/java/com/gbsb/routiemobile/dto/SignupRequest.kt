package com.gbsb.routiemobile.dto
//회원가입 요청 dto
data class SignupRequest(
    val userId: String,
    val email: String,
    val password: String,
    val name: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val weight: Int
)
