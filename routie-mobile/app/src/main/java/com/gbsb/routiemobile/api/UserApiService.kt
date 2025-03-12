package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.LoginRequest
import com.gbsb.routiemobile.dto.LoginResponse
import com.gbsb.routiemobile.dto.SignupRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.*

interface UserApiService {
    // 회원가입 API
    @POST("/api/signup")
    fun registerUser(@Body request: SignupRequest): Call<Map<String, String>>

    // 로그인 API
    @POST("/api/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // 사용자 정보 수정 API
    @PUT("/api/users/{userId}")
    fun updateUser(@Path("userId") userId: String, @Body updatedUser: SignupRequest): Call<SignupRequest>

    // 사용자 계정 삭제 API
    @DELETE("/api/users/{userId}")
    fun deleteUser(@Path("userId") userId: String): Call<String>

}
