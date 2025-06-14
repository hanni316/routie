package com.example.routie_wear.api

import com.example.routie_wear.dto.RoutineLogStartRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RoutineStartApi {
    @POST("/api/routine-logs/start")
    suspend fun startRoutine(@Body dto: RoutineLogStartRequestDto): Long

}