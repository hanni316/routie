package com.example.routie_wear.api

import retrofit2.http.*
import com.example.routie_wear.dto.*
import retrofit2.Response


interface RoutineApi {
        @GET("/api/routine-days")
        suspend fun getRoutinesByDay(
                @Query("userId") userId: String,
                @Query("dayOfWeek") dayOfWeek: String
        ): List<RoutineDto>


        @GET("/api/routine-exercises/watch/{routineId}/exercises")
        suspend fun getWorkoutsByRoutineId(@Path("routineId") routineId: Long): List<WorkoutDto>

        @POST("/exercise-logs")
        suspend fun uploadWorkoutRecord(@Body record: WorkoutRecordDto): Response<Void>

        @POST("/api/routines/complete")
        suspend fun completeRoutine(
                @Body request: RoutineLogRequestDto
        ): Response<Void>

}