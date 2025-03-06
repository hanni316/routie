package com.gbsb.routiemobile.network

import com.gbsb.routiemobile.api.RewardApiService
import com.gbsb.routiemobile.dto.CaloriesRequest
import com.gbsb.routiemobile.dto.RewardResponse
import okio.Timeout
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.45.132:8080" // 실제 서버 주소
    private const val USE_MOCK = true // 서버 없이 테스트할 때 true

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: RewardApiService by lazy {
        retrofit.create(RewardApiService::class.java)
    }

    // 서버 없이 테스트할 때 사용할 Mock API
    private val mockInstance: RewardApiService = object : RewardApiService {
        override fun getUserReward(userID: Int): Call<RewardResponse> {
            return object : Call<RewardResponse> {
                override fun execute(): Response<RewardResponse> {
                    val mockResponse = RewardResponse(userId = 1, totalGold = 100, totalSteps = 5000)
                    return Response.success(mockResponse)
                }
                override fun enqueue(callback: retrofit2.Callback<RewardResponse>?) {}
                override fun isExecuted(): Boolean = false
                override fun cancel() {}
                override fun isCanceled(): Boolean = false
                override fun clone(): Call<RewardResponse> = this
                override fun request(): okhttp3.Request = okhttp3.Request.Builder().url(BASE_URL).build()
                override fun timeout(): Timeout {
                    TODO("Not yet implemented")
                }
            }
        }

        override fun getUserReward(userId: Long): Call<RewardResponse> {
            return getUserReward(userId.toInt()) // 동일한 Mock 데이터 반환
        }

        override fun sendCalories(userId: Int, request: CaloriesRequest): Call<RewardResponse> {
            return object : Call<RewardResponse> {
                override fun execute(): Response<RewardResponse> {
                    val mockResponse = RewardResponse(userId = 1, totalGold = 100, totalSteps = 5000)
                    return Response.success(mockResponse)
                }
                override fun enqueue(callback: retrofit2.Callback<RewardResponse>?) {}
                override fun isExecuted(): Boolean = false
                override fun cancel() {}
                override fun isCanceled(): Boolean = false
                override fun clone(): Call<RewardResponse> = this
                override fun request(): okhttp3.Request = okhttp3.Request.Builder().url(BASE_URL).build()
                override fun timeout(): Timeout {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    // 프론트 개발자가 서버/Mock 전환 가능
    val api: RewardApiService = if (USE_MOCK) mockInstance else instance
}
