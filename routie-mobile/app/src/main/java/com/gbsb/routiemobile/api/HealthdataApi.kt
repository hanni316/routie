package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.HealthDataDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HealthdataApi {
    @POST("health/data") //서버 엔드포인트에 맞춰서 변경하슈
    fun sendHealthData(
        @Body data: HealthDataDto
    ): Call<Void>
}