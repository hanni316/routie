package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.GachaResultDto
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.POST

interface UserItemApi {
    @POST("/api/user/items/gacha")
    fun sendGachaResult(@Body result: GachaResultDto): Call<Void>
}