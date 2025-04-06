package com.example.routie_wear.ui

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import com.example.routie_wear.viewmodel.RoutineViewModel
import com.example.routie_wear.util.VibrationUtil

@Composable
fun WorkoutListScreen(
    viewModel: RoutineViewModel,
    onWorkoutSelected: () -> Unit
) {
    val workouts = viewModel.workoutList
    val selectedWorkout = viewModel.selectedWorkout
    val context = LocalContext.current

    ScalingLazyColumn {
        items(workouts) { workout ->
            val isSelected = workout == selectedWorkout

            val alpha by animateFloatAsState(
                targetValue = if (isSelected) 0.6f else 1f,
                animationSpec = tween(durationMillis = 250)
            )

            Chip(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .animateContentSize(),
                label = {
                    Text(
                        text = "${workout.name}",
                        color = MaterialTheme.colors.onSurface.copy(alpha = alpha)
                    )
                },
                onClick = {
                    VibrationUtil.vibrate(context)  // 진동 효과
                    viewModel.selectedWorkout = workout
                    onWorkoutSelected()
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = if (isSelected)
                        MaterialTheme.colors.secondary
                    else
                        MaterialTheme.colors.surface
                )
            )
        }
    }
}