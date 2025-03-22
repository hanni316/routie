package com.gbsb.routiemobile.dto

data class PasswordUpdateRequest(
    val currentPassword: String,
    val newPassword: String
)
