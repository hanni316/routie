package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.dto.RoutineRequest
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

    // 운동 루틴 수정
    @PUT("/api/routines/{routineId}")
    fun updateRoutine(@Path("routineId") routineId: String, @Body routine: Routine): Call<Routine>

    // 운동 루틴 삭제
    @DELETE("/api/routines/{routineId}")
    fun deleteRoutine(@Path("routineId") routineId: String): Call<Void>
}
