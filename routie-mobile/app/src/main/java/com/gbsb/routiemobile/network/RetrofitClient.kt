package com.gbsb.routiemobile.network

import com.gbsb.routiemobile.api.RewardApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.45.132:8080" // 서버 ip 확인

    val instance: RewardApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RewardApiService::class.java)
    }
}