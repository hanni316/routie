package com.example.routie_wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.routie_wear.viewmodel.RoutineViewModel

@Composable
fun StartScreen(
    viewModel: RoutineViewModel,
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(16.dp)
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text("👋 환영해요!",
            style = MaterialTheme.typography.title2,
            color = MaterialTheme.colors.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onStartClick,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text("루틴 시작하기")
        }
    }
}