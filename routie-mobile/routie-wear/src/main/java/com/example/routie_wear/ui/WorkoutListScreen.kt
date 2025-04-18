package com.example.routie_wear.ui

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val selectedRoutineId = viewModel.selectedRoutineId
    val context = LocalContext.current

    LaunchedEffect(selectedRoutineId) {
        if (selectedRoutineId != null) {
            viewModel.loadWorkouts(selectedRoutineId!!)
        }
    }

    if (workouts.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "운동이 비어 있어요!",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary
            )
        }
    } else {
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
                            text = "${workout.exerciseName}",
                            color = MaterialTheme.colors.onSurface.copy(alpha = alpha)
                        )
                    },
                    onClick = {
                        VibrationUtil.vibrate(context)
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
}