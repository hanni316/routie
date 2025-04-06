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
fun RoutineListScreen(
    viewModel: RoutineViewModel,
    onRoutineSelected: () -> Unit
) {
    val routines = viewModel.routineList
    val context = LocalContext.current
    var selectedId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadTodayRoutines()
    }

    ScalingLazyColumn {
        items(routines) { routine ->
            val isSelected = selectedId == routine.id

            val alphaAnim by animateFloatAsState(
                targetValue = if (isSelected) 0.5f else 1f,
                animationSpec = tween(300)
            )

            Chip(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .animateContentSize(),
                label = {
                    Text(
                        routine.name,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = alphaAnim)
                    )
                },
                onClick = {
                    VibrationUtil.vibrate(context) // 진동 호출
                    selectedId = routine.id
                    viewModel.loadWorkouts(routine.id)
                    onRoutineSelected()
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