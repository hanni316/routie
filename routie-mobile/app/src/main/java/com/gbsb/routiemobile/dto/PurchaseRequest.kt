package com.gbsb.routiemobile.dto

data class PurchaseRequest(
    val userId: String,
    val itemId: Int,
    val quantity: Int
)
