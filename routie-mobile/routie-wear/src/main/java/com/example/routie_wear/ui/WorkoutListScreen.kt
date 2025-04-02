package com.example.routie_wear.ui

import androidx.compose.runtime.Composable
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text
import com.example.routie_wear.viewmodel.RoutineViewModel

@Composable
fun WorkoutListScreen(viewModel: RoutineViewModel, onWorkoutSelected: () -> Unit) {
    val workouts = viewModel.workoutList

    ScalingLazyColumn {
        items(workouts) { workout ->
            Chip(
                label = { Text(workout.name) },
                onClick = {
                    viewModel.selectedWorkout = workout
                    onWorkoutSelected()
                }
            )
        }
    }
}