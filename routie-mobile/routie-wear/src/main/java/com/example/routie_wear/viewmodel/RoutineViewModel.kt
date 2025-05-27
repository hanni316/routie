package com.example.routie_wear.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routie_wear.dto.*
import com.example.routie_wear.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.time.LocalDate
import android.util.Log

class RoutineViewModel : ViewModel() {

    var userId by mutableStateOf<String?>(null)

    var routineList by mutableStateOf<List<RoutineDto>>(emptyList())
    var workoutList by mutableStateOf<List<WorkoutDto>>(emptyList())
        internal set

    var isLoading by mutableStateOf(true)
        private set

    var selectedRoutineId: Long? = null
    var selectedWorkout: WorkoutDto? = null

    var timerSeconds by mutableStateOf(0)
    var isRunning by mutableStateOf(false)

    // 🔥 새로 추가된 루틴 로그 ID (서버로부터 받는 것)
    var routineLogId: Long? = null

    private val api = RetrofitInstance.api
    private val routineStartApi = RetrofitInstance.routineStartApi

    //운동 중 기록을 저장하는 리스트
    private val tempWorkoutRecords = mutableListOf<WorkoutRecordDto>()

    // 오늘 루틴 목록 불러오기
    fun loadTodayRoutines() {
        val today = LocalDate.now().dayOfWeek.name.lowercase()
        val uid = userId ?: run {
            Log.e("VM", "userId가 null입니다. 루틴을 불러올 수 없습니다.")
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                routineList = api.getRoutinesByDay(uid, today)
                Log.d("VM", "받아온 루틴 개수: ${routineList.size}")
            } catch (e: Exception) {
                Log.e("VM", "루틴 조회 에러", e)
                routineList = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    // 루틴 선택 후 운동 목록 불러오기
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

    // 운동 한 개 기록을 로컬에 저장
    fun saveWorkoutLocally(duration: Int) {
        val exercise = selectedWorkout ?: return

        val record = WorkoutRecordDto(
            routineLogId = 0L,  // 아직은 dummy
            exerciseId = exercise.exerciseId,
            duration = duration
        )
        tempWorkoutRecords.add(record)
        Log.d("VM", "운동 로컬 저장: ${record.exerciseId}, ${record.duration}초")
    }

    // 루틴 종료 시 서버에 로그 생성 + 운동 기록 업로드
    fun completeRoutineAndUpload(onComplete: () -> Unit = {}) {
        val routineId = selectedRoutineId ?: return
        val uid = userId ?: return

        val exerciseDtos = tempWorkoutRecords.map {
            ExerciseLogRequestDto(
                exerciseId = it.exerciseId,
                duration = it.duration
            )
        }.also {
            Log.d("VM", "서버로 보낼 운동 개수: ${it.size}")
            it.forEach { e ->
                Log.d("VM", "운동 ID=${e.exerciseId}, duration=${e.duration}")
            }
        }

        val request = RoutineLogRequestDto(
            routineId = routineId,
            userId = uid,
            exercises = exerciseDtos
        )

        viewModelScope.launch {
            try {
                val response = api.completeRoutine(request)
                if (response.isSuccessful) {
                    Log.d("VM", "루틴 종료 기록 완료")
                    tempWorkoutRecords.clear()
                    onComplete()
                } else {
                    Log.e("VM", "루틴 업로드 실패 code=${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("VM", "루틴 업로드 중 예외", e)
            }
        }
    }

    fun resetTimer() {
        timerSeconds = 0
        isRunning = false
    }
}