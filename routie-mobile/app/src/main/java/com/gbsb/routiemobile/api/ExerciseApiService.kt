package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Exercise
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApiService {

    // 모든 운동 목록 조회
    @GET("/api/exercises")
    fun getAllExercises(): Call<List<Exercise>>

    // 특정 카테고리에 속한 운동 조회
    @GET("/api/exercises/category/{categoryId}")
    fun getExercisesByCategory(@Path("categoryId") categoryId: Long): Call<List<Exercise>>
}
