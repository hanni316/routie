package com.gbsb.routiemobile.network

import com.gbsb.routiemobile.api.RewardApiService
import com.gbsb.routiemobile.api.RoutineApiService
import com.gbsb.routiemobile.api.ExerciseApiService
import com.gbsb.routiemobile.api.RoutineLogApi
import com.gbsb.routiemobile.dto.CaloriesRequest
import com.gbsb.routiemobile.dto.RewardResponse
import com.gbsb.routiemobile.api.UserApiService
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.45.132:8080/"
    // 실제 서버 주소 http://192.168.45.132:8080/
    //http://172.30.1.75:8080/
    // 에뮬레이터에서 실행 "http://10.0.2.2:8080/"
    private const val USE_MOCK = false // 서버 없이 테스트할 때 true

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val instance: RewardApiService by lazy {
        retrofit.create(RewardApiService::class.java)
    }

    val routineApi: RoutineApiService by lazy {
        retrofit.create(RoutineApiService::class.java)
    }

    val exerciseApi: ExerciseApiService by lazy {
        retrofit.create(ExerciseApiService::class.java)
    }

    val routineLogApi: RoutineLogApi = retrofit.create(RoutineLogApi::class.java)

    // 🛠 Mock API: 서버 없이 개발할 수 있도록 가짜 데이터 제공
    private val mockInstance: RewardApiService = object : RewardApiService {

        override fun getUserReward(userId: String): Call<RewardResponse> {
            return object : Call<RewardResponse> {
                override fun execute(): Response<RewardResponse> {
                    val mockResponse = RewardResponse(userId = userId, totalGold = 500, totalSteps = 10000)
                    return Response.success(mockResponse)
                }
                override fun enqueue(callback: Callback<RewardResponse>?) {}
                override fun isExecuted(): Boolean = false
                override fun cancel() {}
                override fun isCanceled(): Boolean = false
                override fun clone(): Call<RewardResponse> = this
                override fun request(): Request = Request.Builder().url(BASE_URL).build()
                override fun timeout(): Timeout = Timeout.NONE
            }
        }

        override fun sendCalories(userId: String, request: CaloriesRequest): Call<RewardResponse> {
            return object : Call<RewardResponse> {
                override fun execute(): Response<RewardResponse> {
                    val mockResponse = RewardResponse(userId = userId, totalGold = 700, totalSteps = 15000)
                    return Response.success(mockResponse)
                }
                override fun enqueue(callback: Callback<RewardResponse>?) {}
                override fun isExecuted(): Boolean = false
                override fun cancel() {}
                override fun isCanceled(): Boolean = false
                override fun clone(): Call<RewardResponse> = this
                override fun request(): Request = Request.Builder().url(BASE_URL).build()
                override fun timeout(): Timeout = Timeout.NONE
            }
        }
    }

    // 프론트 개발자가 서버/Mock 전환 가능
    val api: RewardApiService = if (USE_MOCK) mockInstance else instance
}
