package com.example.routie_wear.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.routie_wear.api.RoutineApi

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://172.16.111.68:8080") // 실제 서버 주소 넣기
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RoutineApi by lazy {
        retrofit.create(RoutineApi::class.java)
    }
}