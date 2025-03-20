package com.gbsb.routiemobile.dto

data class User(
    val userId: String,
    val email: String,
    val password: String,
    val name: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val weight: Int
)
