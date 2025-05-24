package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Achievement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AchievementApi {
    @GET("/api/achievements/user/{userId}")
    fun getUserAchievements(@Path("userId") userId: String): Call<List<Achievement>>
}