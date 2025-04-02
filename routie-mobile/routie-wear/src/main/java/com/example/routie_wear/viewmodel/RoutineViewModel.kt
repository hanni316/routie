package com.example.routie_wear.viewmodel

import com.example.routie_wear.dto.*
import com.example.routie_wear.network.RetrofitInstance
import com.example.routie_wear.api.RoutineApi
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RoutineViewModel : ViewModel() {
    var routineList by mutableStateOf<List<RoutineDto>>(emptyList())
    var workoutList by mutableStateOf<List<WorkoutDto>>(emptyList())
    var selectedRoutineId: Long? = null
    var selectedWorkout: WorkoutDto? = null
    var timerSeconds by mutableStateOf(0)
    var isRunning by mutableStateOf(false)

    private val api = RetrofitInstance.api

    fun loadTodayRoutines() {
        val today = LocalDate.now().dayOfWeek.name.lowercase()
        viewModelScope.launch {
            routineList = api.getRoutinesByDay(today)
        }
    }

    fun loadWorkouts(routineId: Long) {
        selectedRoutineId = routineId
        viewModelScope.launch {
            workoutList = api.getWorkoutsByRoutineId(routineId)
        }
    }

    fun uploadWorkout() {
        val workout = selectedWorkout ?: return
        val routineId = selectedRoutineId ?: return

        val record = WorkoutRecordDto(
            routineId = routineId,
            workoutName = workout.name,
            durationSeconds = timerSeconds
        )

        viewModelScope.launch {
            api.uploadWorkoutRecord(record)
        }
    }

    fun resetTimer() {
        timerSeconds = 0
        isRunning = false
    }
}