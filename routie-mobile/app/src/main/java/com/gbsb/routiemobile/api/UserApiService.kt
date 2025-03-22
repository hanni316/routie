package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.LoginRequest
import com.gbsb.routiemobile.dto.LoginResponse
import com.gbsb.routiemobile.dto.PasswordUpdateRequest
import com.gbsb.routiemobile.dto.SignupRequest
import com.gbsb.routiemobile.dto.User
import com.gbsb.routiemobile.dto.UserUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    // 회원가입 API
    @POST("/api/signup")
    fun registerUser(@Body request: SignupRequest): Call<Map<String, String>>

    // 로그인 API
    @POST("/api/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // 회원 정보 조회
    @GET("/api/users/{userId}")
    fun getUser(@Path("userId") userId: String): Call<User>

    // 회원 정보 변경
    @PUT("/api/users/{userId}")
    fun updateUser(@Path("userId") userId: String, @Body user: UserUpdateRequest): Call<User>

    // 비밀번호 변경
    @PUT("/api/users/{userId}/password")
    fun updatePassword(
        @Path("userId") userId: String,
        @Body request: PasswordUpdateRequest
    ): Call<Void>

    // 회원 탈퇴
    @DELETE("/api/users/{userId}")
    fun deleteUser(@Path("userId") userId: String): Call<Void>
}
