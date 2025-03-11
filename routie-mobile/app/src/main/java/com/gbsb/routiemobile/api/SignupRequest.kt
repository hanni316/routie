package com.gbsb.routiemobile.api
//회원가입 요청 dto
data class SignupRequest(
    val userId: String,
    val email: String,
    val password: String,
    val name: String
)
