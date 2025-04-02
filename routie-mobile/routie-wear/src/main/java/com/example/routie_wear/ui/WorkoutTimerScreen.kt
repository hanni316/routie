package com.example.routie_wear.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.routie_wear.viewmodel.RoutineViewModel
import kotlinx.coroutines.delay

@Composable
fun WorkoutTimerScreen(viewModel: RoutineViewModel, onFinish: () -> Unit) {
    val isRunning = viewModel.isRunning
    val time = viewModel.timerSeconds

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000)
                viewModel.timerSeconds++
            }
        }
    }

    Column {
        Text(
            "운동 시간: ${time}초",
            style = MaterialTheme.typography.body1
            )
        Button(onClick = {
            if (!isRunning) {
                viewModel.isRunning = true
            } else {
                viewModel.uploadWorkout()
                viewModel.resetTimer()
                onFinish()
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