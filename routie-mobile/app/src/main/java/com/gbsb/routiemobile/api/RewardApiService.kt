package com.gbsb.routiemobile.api;

import com.gbsb.routiemobile.dto.RewardResponse
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface RewardApiService {
    @GET("/api/reward/{userId}")
    fun getUserReward(@Path("userId") userID: Int): Call<RewardResponse>
}
