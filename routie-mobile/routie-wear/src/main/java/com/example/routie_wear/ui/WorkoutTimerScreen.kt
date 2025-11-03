package com.example.routie_wear.ui

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.routie_wear.sensor.StepCache
import com.example.routie_wear.util.VibrationUtil
import com.example.routie_wear.viewmodel.RoutineViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 단일 운동에 대해 Composable 내에서 독립 타이머를 관리하고,
 * 완료 시 애니메이션 메시지를 보여주는 UI
 */
@Composable
fun WorkoutTimerScreen(
    viewModel: RoutineViewModel,
    onFinish: () -> Unit = {}
) {
    val workout = viewModel.selectedWorkout
    if (workout == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "운동을 선택해주세요.",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
        return
    }

    // 로컬에서 관리하는 경과 시간
    var elapsed by rememberSaveable { mutableStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    var showCompletionMessage by remember { mutableStateOf(false) }
    var triggerFinish by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 완료 후 네비게이션 또는 콜백
    if (triggerFinish) {
        LaunchedEffect(Unit) {
            delay(2000)
            showCompletionMessage = false
            triggerFinish = false
            onFinish()
        }
    }

    // 애니메이션 값
    val alpha by animateFloatAsState(
        targetValue = if (showCompletionMessage) 1f else 0f,
        animationSpec = tween(700)
    )
    val scale by animateFloatAsState(
        targetValue = if (showCompletionMessage) 1.2f else 0.8f,
        animationSpec = tween(700)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showCompletionMessage) {
            //여기서 세션 결과 걸음수 보여줌
            // lastWalkResultSteps에 서버에서 계산된 delta steps 들어옴
            Text(
                text = if (viewModel.lastWalkResultSteps != null){
                    "걷기: ${viewModel.lastWalkResultSteps}걸음"
                } else {
                    "운동 끝!"
                },
                style = MaterialTheme.typography.title2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.graphicsLayer(
                    alpha = alpha,
                    scaleX = scale,
                    scaleY = scale
                )
            )
        } else {
            // 타이머 표시
            Text(
                text = "⏱${elapsed}초",
                style = MaterialTheme.typography.display1,
                color = MaterialTheme.colors.primary
            )
            Button(
                onClick = {
                    if (!isRunning) {
                        // ===세션시작===
                        VibrationUtil.vibrate(context)
                        isRunning = true

                        //(1)현재 누적 스텝(baseline) 가져오기
                        val startSteps = StepCache.lastKnownTotalSteps

                        //(2)ViewModel에게 '걷기 세션 시작' 알리기
                        //     -> 서버 /walk-sessions/start 호출해서 sessionId 받아옴
                        viewModel.startWalkSession(
                            startStepCount = startSteps
                        )

                        timerJob?.cancel()
                        timerJob = scope.launch {
                            while (isRunning) {
                                delay(1000)
                                elapsed++
                            }
                        }
                    } else {
                        //===세션 종료===
                            // 타이머 정지 및 서버 업로드
                        VibrationUtil.vibrate(context)
                        timerJob?.cancel()
                        isRunning = false

                        //(1) 종료 시점 걸음수 스냅샷
                        val endSteps = StepCache.lastKnownTotalSteps

                        //(2) ViewModel에게 '걷기 세션 종료' 알리기
                        //     -> 서버 /walk-sessions/end 호출 (delta 계산 & 저장)
                        //     durationSeconds=elapsed 는 기존 운동 기록 용으로도 쓸 수 있음
                        viewModel.endWalkSession(
                            endStepCount = endSteps,
                            durationSeconds = elapsed
                        )

                        //기본 로직 유지: 칼로리용 운동 기록 로컬 저장
                        viewModel.saveWorkoutLocally(elapsed)
                        // 로컬에서 초기화
                        elapsed = 0
                        showCompletionMessage = true
                        triggerFinish = true
                        VibrationUtil.vibrate(context, 300)
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(if (!isRunning) "시작" else "정지")
            }
        }
    }
}
