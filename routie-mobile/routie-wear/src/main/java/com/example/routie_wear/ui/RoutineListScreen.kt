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
fun RoutineListScreen(
    viewModel: RoutineViewModel,
    onRoutineSelected: () -> Unit
) {
    val context = LocalContext.current
    var selectedId by remember { mutableStateOf<Long?>(null) }

    val routines = viewModel.routineList
    val isLoading = viewModel.isLoading  // <- ViewModel에서 상태 추가 필요!(완료)

    // 루틴 처음 불러오기
    LaunchedEffect(Unit) {
        viewModel.loadTodayRoutines()
    }

    when {
        isLoading -> {
            // 로딩 중일 때
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        routines.isEmpty() -> {
            // 불러왔는데 아무 루틴도 없을 때
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "오늘 등록된 루틴이 없어요!",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary)
            }
        }

        else -> {
            // 루틴 목록 보여줄 때
            ScalingLazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ){
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
                                color = MaterialTheme.colors.onPrimary
                            )
                        },
                        onClick = {
                            VibrationUtil.vibrate(context)
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
    }
}