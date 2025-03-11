package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Routine
import retrofit2.Call
import retrofit2.http.*

interface RoutineApiService {
    // 운동 루틴 조회 (사용자 ID 기반)
    @GET("/api/admin/routines/{userId}")
    fun getUserRoutines(@Path("userId") userId: String): Call<List<Routine>>

    // 운동 루틴 생성
    @POST("/api/admin/routines/{userId}")
    fun createRoutine(@Path("userId") userId: String, @Body routine: Routine): Call<Routine>

    // 운동 루틴 수정
    @PUT("/api/admin/routines/{routineId}")
    fun updateRoutine(@Path("routineId") routineId: String, @Body routine: Routine): Call<Routine>

    // 운동 루틴 삭제
    @DELETE("/api/admin/routines/{routineId}")
    fun deleteRoutine(@Path("routineId") routineId: String): Call<Void>
}
