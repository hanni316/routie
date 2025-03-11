package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.CaloriesRequest
import com.gbsb.routiemobile.dto.RewardResponse
import retrofit2.Call
import retrofit2.http.*

interface RewardApiService {

    // 리워드 조회 (GET /api/reward/{userId})
    @GET("/api/reward/{userId}")
    fun getUserReward(@Path("userId") userId: String): Call<RewardResponse>

    // 리워드 지급 (POST /api/reward/{userId}/calories)
    @POST("/api/reward/{userId}/calories")
    fun sendCalories(@Path("userId") userId: String, @Body request: CaloriesRequest): Call<RewardResponse>
}
