package com.example.routie_wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.wear.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.routie_wear.viewmodel.RoutineViewModel

@Composable
fun StartScreen(
    viewModel: RoutineViewModel,
    onStartClick: () -> Unit
) {
    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "👋 환영해요!",
                style = MaterialTheme.typography.title1,
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onStartClick,
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    "루틴 시작하기",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}