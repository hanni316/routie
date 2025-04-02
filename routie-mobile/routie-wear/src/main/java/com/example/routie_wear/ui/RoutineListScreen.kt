package com.example.routie_wear.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import com.example.routie_wear.viewmodel.RoutineViewModel

@Composable
fun RoutineListScreen(viewModel: RoutineViewModel, onRoutineSelected: () -> Unit) {
    val routines = viewModel.routineList

    LaunchedEffect(Unit) {
        viewModel.loadTodayRoutines()
    }

    ScalingLazyColumn {
        items(routines) { routine ->
            Chip(
                label = { Text(routine.name) },
                onClick = {
                    viewModel.loadWorkouts(routine.id)
                    onRoutineSelected()
                }
            )
        }
    }
}