package com.example.routie_wear.ui

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.example.routie_wear.util.VibrationUtil
import com.example.routie_wear.viewmodel.RoutineViewModel
import kotlinx.coroutines.delay

@Composable
fun WorkoutTimerScreen(
    viewModel: RoutineViewModel,
    onFinish: () -> Unit
) {
    val isRunning = viewModel.isRunning
    val time = viewModel.timerSeconds
    val context = LocalContext.current

    var showCompletionMessage by remember { mutableStateOf(false) }
    var triggerFinish by remember { mutableStateOf(false) }

    if (triggerFinish) {
        LaunchedEffect(Unit) {
            delay(2000)
            showCompletionMessage = false
            onFinish()
        }
    }

    // 애니메이션 효과
    val alpha by animateFloatAsState(
        targetValue = if (showCompletionMessage) 1f else 0f,
        animationSpec = tween(700)
    )
    val scale by animateFloatAsState(
        targetValue = if (showCompletionMessage) 1.2f else 0.8f,
        animationSpec = tween(700)
    )

    // 타이머 작동
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000)
                viewModel.timerSeconds++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showCompletionMessage) {
            Text(
                text = "☺️ 운동 끝! 최고야! 👍",
                style = MaterialTheme.typography.title2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .graphicsLayer(
                        alpha = alpha,
                        scaleX = scale,
                        scaleY = scale
                    )
            )
        } else {
            Text(
                text = "⏱${time}초",
                style = MaterialTheme.typography.display1,
                color = MaterialTheme.colors.primary
            )

            Button(
                onClick = {
                    if (!isRunning) {
                        VibrationUtil.vibrate(context)
                        viewModel.userId?.let { userId ->
                            viewModel.startRoutineLog(userId = userId) {
                                viewModel.isRunning = true
                            }
                        }
                    } else {
                        VibrationUtil.vibrate(context) // 진동
                        viewModel.uploadWorkout()
                        viewModel.resetTimer()
                        showCompletionMessage = true
                        triggerFinish = true

                        // 추가 진동 효과
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