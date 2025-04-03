package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.dto.RoutineRequest
import com.gbsb.routiemobile.dto.RoutineResponse
import com.gbsb.routiemobile.dto.RoutineUpdateRequest
import com.gbsb.routiemobile.dto.ExerciseResponse
import retrofit2.Call
import retrofit2.http.*

interface RoutineApiService {
    //운동 루틴 생성
    @POST("api/routines/{userId}")
    fun createRoutineWithExercises(
        @Path("userId") userId: String, @Body routineRequest: RoutineRequest): Call<Routine>

    // 운동 루틴 조회 (사용자 ID 기반)
    @GET("/api/routines/{userId}")
    fun getUserRoutines(@Path("userId") userId: String): Call<List<Routine>>

    // 운동 루틴 수정(루틴 이름, 설명, 요일)
    @PUT("api/routines/{routineId}")
    fun updateRoutine(
        @Path("routineId") routineId: String,
        @Body routineUpdateRequest: RoutineUpdateRequest): Call<RoutineResponse>

    // 단일 루틴 상세 조회
    @GET("api/routines/detail/{routineId}")
    fun getRoutineDetail(@Path("routineId") routineId: String): Call<RoutineResponse>

    // 특정 루틴에 운동 추가
    @POST("/api/routine-exercises/{routineId}/exercises/{exerciseId}")
    fun addExerciseToRoutine(@Path("routineId") routineId: String,
        @Path("exerciseId") exerciseId: String): Call<ExerciseResponse>

    // 특정 루틴의 운동 목록 조회
    @GET("api/routine-exercises/{routineId}/exercises")
    fun getExercisesByRoutine(@Path("routineId") routineId: String): Call<List<ExerciseResponse>>

    // 특정 루틴에서 운동 삭제
    @DELETE("api/routine-exercises/exercises/{routineExerciseId}")
    fun removeExerciseFromRoutine(@Path("routineExerciseId") routineExerciseId: Long): Call<String>

    // 운동 루틴 삭제
    @DELETE("/api/routines/{routineId}")
    fun deleteRoutine(@Path("routineId") routineId: String): Call<Void>
}
