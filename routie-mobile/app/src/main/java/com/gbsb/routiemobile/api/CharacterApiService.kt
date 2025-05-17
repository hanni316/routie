package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import com.gbsb.routiemobile.dto.CharacterStyleRequestDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.PUT

interface CharacterApiService {
    @GET("/api/character/{userId}")
    fun getStyle(@Path("userId") userId: String): Call<CharacterStyleResponseDto>

    @PUT("/api/character/{userId}")
    fun saveStyle(
        @Path("userId") userId: String,
        @Body dto: CharacterStyleRequestDto
    ): Call<Void>
}