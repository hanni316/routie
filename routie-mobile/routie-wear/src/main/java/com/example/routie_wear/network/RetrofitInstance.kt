package com.example.routie_wear.network

import com.example.routie_wear.api.RewardApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.routie_wear.api.RoutineApi
import com.example.routie_wear.api.RoutineStartApi

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.45.132:8080") // 실제 서버 주소 넣기
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RoutineApi by lazy {
        retrofit.create(RoutineApi::class.java)
    }

    val routineStartApi: RoutineStartApi by lazy {
        retrofit.create(RoutineStartApi::class.java)
    }

    val rewardApi: RewardApi by lazy {
        retrofit.create(RewardApi::class.java)
    }
}