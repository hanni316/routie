package com.example.routie_wear.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routie_wear.dto.*
import com.example.routie_wear.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.time.LocalDate

class RoutineViewModel : ViewModel() {

    var userId by mutableStateOf<String?>(null)

    var routineList by mutableStateOf<List<RoutineDto>>(emptyList())
    var workoutList by mutableStateOf<List<WorkoutDto>>(emptyList())

    var selectedRoutineId: Long? = null
    var selectedWorkout: WorkoutDto? = null

    var timerSeconds by mutableStateOf(0)
    var isRunning by mutableStateOf(false)

    // 🔥 새로 추가된 루틴 로그 ID (서버로부터 받는 것)
    var routineLogId: Long? = null

    private val api = RetrofitInstance.api
    private val routineStartApi = RetrofitInstance.routineStartApi

    // ✅ 오늘 루틴 목록 불러오기
    fun loadTodayRoutines() {
        val today = LocalDate.now().dayOfWeek.name.lowercase()
        viewModelScope.launch {
            try {
                routineList = api.getRoutinesByDay(today)
            } catch (e: Exception) {
                println("루틴 목록 불러오기 실패: ${e.message}")
            }
        }
    }

    // ✅ 루틴 선택 후 운동 목록 불러오기
    fun loadWorkouts(routineId: Long) {
        selectedRoutineId = routineId
        viewModelScope.launch {
            try {
                workoutList = api.getWorkoutsByRoutineId(routineId)
            } catch (e: Exception) {
                println("운동 목록 불러오기 실패: ${e.message}")
            }
        }
    }

    // ✅ 루틴 시작 시 서버에 RoutineLog 생성 요청
    fun startRoutineLog(userId: String, onStarted: () -> Unit) {
        val routineId = selectedRoutineId ?: return
        val uid = userId ?: run {
            println("사용자 아이디가 설정되지 않았습니다!")
            return
        }
        viewModelScope.launch {
            try {
                val response = routineStartApi.startRoutine(
                    RoutineLogStartRequestDto(routineId, userId)
                )
                routineLogId = response
                println("루틴 시작 완료! routineLogId: $routineLogId")
                onStarted()
            } catch (e: Exception) {
                println("루틴 시작 실패: ${e.message}")
            }
        }
    }

    // ✅ 운동 기록 서버에 업로드
    fun uploadWorkout() {
        val exercise = selectedWorkout ?: return
        val logId = routineLogId ?: return

        val record = WorkoutRecordDto(
            routineLogId = logId,
            exerciseId = exercise.id,
            duration = timerSeconds
        )

        viewModelScope.launch {
            try {
                api.uploadWorkoutRecord(record)
                println("운동 기록 업로드 완료")
            } catch (e: Exception) {
                println("업로드 실패: ${e.message}")
            }
        }
    }

    fun resetTimer() {
        timerSeconds = 0
        isRunning = false
    }
}