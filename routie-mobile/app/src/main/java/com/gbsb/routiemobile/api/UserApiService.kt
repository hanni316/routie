package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.LoginRequest
import com.gbsb.routiemobile.dto.LoginResponse
import com.gbsb.routiemobile.dto.PasswordUpdateRequest
import com.gbsb.routiemobile.dto.ProfileImageResponseDto
import com.gbsb.routiemobile.dto.SignupRequest
import com.gbsb.routiemobile.dto.User
import com.gbsb.routiemobile.dto.UserUpdateRequest
import com.gbsb.routiemobile.dto.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/api/users/check-id")
    fun checkUserId(@Query("userId") userId: String): Call<Boolean>

    @Multipart
    @POST("/api/{userId}/profile-image")
    fun uploadProfileImage(
        @Path("userId") userId: String,
        @Part file: MultipartBody.Part
    ): Call<ProfileImageResponseDto>

    @GET("api/{id}/profile")
    fun getUserProfile(@Path("id") userId: String): Call<UserProfileResponse>

}
