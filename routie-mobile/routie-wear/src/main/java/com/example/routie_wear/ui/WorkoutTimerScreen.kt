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
    // 현재 선택된 운동
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

    // 운동 ID
    val id = workout.routineExerciseId
    // Composable 내 타이머 상태
    var elapsed by rememberSaveable { mutableStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    // 완료 메시지 표시 제어
    var showCompletionMessage by remember { mutableStateOf(false) }
    var triggerFinish by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 완료 후 네비게이션/업로드 등 onFinish 호출
    if (triggerFinish) {
        LaunchedEffect(Unit) {
            delay(2000)
            showCompletionMessage = false
            triggerFinish = false
            onFinish()
        }
    }

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
            Text(
                text = " ☺️운동 끝! 최고야!",
                style = MaterialTheme.typography.title2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.graphicsLayer(
                    alpha = alpha,
                    scaleX = scale,
                    scaleY = scale
                )
            )
        } else {
            Text(
                text = "⏱${elapsed}초",
                style = MaterialTheme.typography.display1,
                color = MaterialTheme.colors.primary
            )
            // 시작/정지 버튼
            Button(
                onClick = {
                    if (!isRunning) {
                        // 타이머 시작
                        VibrationUtil.vibrate(context)
                        isRunning = true
                        timerJob?.cancel()
                        timerJob = scope.launch {
                            while (isRunning) {
                                delay(1000)
                                elapsed++
                            }
                        }
                    } else {
                        // 타이머 정지 및 완료 처리
                        VibrationUtil.vibrate(context)
                        timerJob?.cancel()
                        isRunning = false
                        // 네트워크 업로드는 ViewModel에 위임
                        viewModel.uploadWorkout()
                        // composable 상태 리셋
                        viewModel.resetTimer()
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