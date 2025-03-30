package com.gbsb.routiemobile.api

import retrofit2.Call
import com.gbsb.routiemobile.dto.RoutineLog
import retrofit2.http.GET
import retrofit2.http.Query

interface RoutineLogApi {
    @GET("api/routines/logs")
    fun getRoutineLogsByDate(
        @Query("userId") userId: String,
        @Query("date") date: String
    ): Call<List<RoutineLog>>
}