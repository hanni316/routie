package com.example.routie_wear.viewmodel

import WalkSessionEndRequestDto
import WalkSessionStartRequestDto
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

    // 걷기 세션 관련 상태 ------------------
    var currentWalkSessionId by mutableStateOf<Long?>(null) // 서버에서 받은 sessionId
        private set

    private var walkSessionStartTime: Long? = null // 워치에서 세션 시작한 시간(ms)

    // UI에서 결과 보여주기 용: "걷기: XXX걸음" 이 XXX를 저장
    var lastWalkResultSteps by mutableStateOf<Int?>(null)
        private set

    // 걷기 시작 시 찍은 시작 걸음수 (워치 내부 상태)
    // -> 서버에도 보내긴 하지만, 로컬에도 들고 있으면 즉시 디버깅 가능
    var baselineStepsAtStart by mutableStateOf<Int?>(null)
        private set

    private val walkApi = RetrofitInstance.walkSessionApi

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

    /**
     * 걷기 운동 세션 시작:
     * - 현재 걸음수(startStepCount)를 받아서 서버에 세션 생성 요청
     * - sessionId를 받아서 보관
     * - 시작 시간 기록
     */
    fun startWalkSession(
        startStepCount: Int
    ) {
        val uid = userId ?: return
        val routineLog = routineLogId ?: return          // 현재 루틴 로그 ID (혹시 아직 없으면? 아래 설명 참고)
        val exercise = selectedWorkout ?: return

        viewModelScope.launch {
            try {
                val res = walkApi.startWalkSession(
                    WalkSessionStartRequestDto(
                        userId = uid,
                        exerciseId = exercise.exerciseId,
                        startStepCount = startStepCount
                    )
                )
                currentWalkSessionId = res.sessionId
                baselineStepsAtStart = startStepCount
                walkSessionStartTime = System.currentTimeMillis()

                Log.d("VM", "걷기 세션 시작 서버에 등록 sessionId=${res.sessionId}, baseline=$startStepCount")
            } catch (e: Exception) {
                Log.e("VM", "걷기 세션 시작 실패: ${e.message}")
            }
        }
    }

    /**
     * 걷기 운동 세션 종료:
     * - endStepCount(종료 시 걸음수)를 서버에 보내서 delta 계산/저장
     * - 서버가 계산한 stepsDuringSession을 lastWalkResultSteps에 담음
     * - 이후 UI에서 걸음수 표시 가능
     */
    fun endWalkSession(
        endStepCount: Int,
        durationSeconds: Int
    ) {
        val sessionId = currentWalkSessionId ?: return
        val startMs = walkSessionStartTime ?: System.currentTimeMillis()
        val durationMs = System.currentTimeMillis() - startMs

        // 혹시 durationSeconds랑 durationMs가 다를 수 있는데
        // durationMs를 서버에 주고, local durationSeconds는 기존 루틴 업로드에 계속 쓸 수 있어.
        // Routie의 칼로리 로직은 durationSeconds만 보면 되니까 그대로 유지 가능.

        viewModelScope.launch {
            try {
                val res = walkApi.endWalkSession(
                    WalkSessionEndRequestDto(
                        sessionId = sessionId,
                        endStepCount = endStepCount,
                        durationMillis = durationMs
                    )
                )

                lastWalkResultSteps = res.stepsDuringSession
                Log.d("VM", "걷기 세션 종료. 이번 세션 걸음수=${res.stepsDuringSession}")

                // 세션 정리
                currentWalkSessionId = null
                walkSessionStartTime = null
                baselineStepsAtStart = null

            } catch (e: Exception) {
                Log.e("VM", "걷기 세션 종료 실패: ${e.message}")
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