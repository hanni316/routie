package com.example.routie_wear.api

import retrofit2.http.*
import com.example.routie_wear.dto.*


interface RoutineApi {
        @GET("/routines")
        suspend fun getRoutinesByDay(@Query("dayOfWeek") dayOfWeek: String): List<RoutineDto>

        @GET("/routines/{id}/workouts")
        suspend fun getWorkoutsByRoutineId(@Path("id") routineId: Long): List<WorkoutDto>

        @POST("/workout-records")
        suspend fun uploadWorkoutRecord(@Body record: WorkoutRecordDto)

}