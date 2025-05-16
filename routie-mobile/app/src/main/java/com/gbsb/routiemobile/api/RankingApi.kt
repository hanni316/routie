package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.RankingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingApi {
    @GET("/api/ranking")
    suspend fun getRanking(
        @Query("userId") userId: String,
        @Query("type") type: String = "calories"
    ): Response<RankingResponse>
}