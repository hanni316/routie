package com.gbsb.routiemobile.dto

data class UserUpdateRequest(
    val email: String,
    val name: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val weight: Int,
    val password: String? = null
)
