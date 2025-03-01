package com.gbsb.routiemobile.api;

import com.gbsb.routiemobile.dto.CaloriesRequest
import com.gbsb.routiemobile.dto.RewardResponse
import retrofit2.Call;
import retrofit2.http.Body
import retrofit2.http.GET;
import retrofit2.http.POST
import retrofit2.http.Path;

interface RewardApiService {
    @GET("/api/reward/{userId}")
    fun getUserReward(@Path("userId") userID: Int): Call<RewardResponse>

    @POST("/api/reward/{userId}/calories")
    fun sendCalories(@Path("userId") userId: Int, @Body request: CaloriesRequest): Call<RewardResponse>

    @GET("/api/reward/{userId}")
    fun getUserReward(@Path("userId") userId: Long): Call<RewardResponse>
}
