package com.example.routie_wear.api

import retrofit2.http.*
import com.example.routie_wear.dto.*


interface RoutineApi {
        @GET("/api/routines")
        suspend fun getRoutinesByDay(@Query("dayOfWeek") dayOfWeek: String): List<RoutineDto>

        @GET("/api/routines/{id}/workouts")
        suspend fun getWorkoutsByRoutineId(@Path("id") routineId: Long): List<WorkoutDto>

        @POST("/api/exercise-logs")
        suspend fun uploadWorkoutRecord(@Body record: WorkoutRecordDto)

}