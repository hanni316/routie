package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApiService {
    @GET("/api/character/{userId}")
    fun getStyle(@Path("userId") userId: String): Call<CharacterStyleResponseDto>
}