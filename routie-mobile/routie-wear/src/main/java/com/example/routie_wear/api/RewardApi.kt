package com.example.routie_wear.api

import com.example.routie_wear.dto.StepGoalDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface RewardApi {
    @POST("/api/reward/{userId}/steps")
    suspend fun rewardSteps(
        @Path("userId") userId: String,
        @Body request: StepGoalDto
    ): Response<ResponseBody>
}