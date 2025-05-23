package com.example.routie_wear.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.routie_wear.viewmodel.RoutineViewModel

@Composable
fun MainNav(viewModel: RoutineViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "start") {

        composable("start") {
            StartScreen(viewModel){
                navController.navigate("routine")  // 루틴 목록으로 이동
            }
        }

        composable("routine") {
            RoutineListScreen(viewModel) {
                navController.navigate("workouts")
            }
        }
        composable("workouts") {
            WorkoutListScreen(viewModel) {
                navController.navigate("timer")
            }
        }
        composable("timer") {
            WorkoutTimerScreen(viewModel) {
                navController.navigate("workouts") // 운동 끝 → 루틴 화면으로 되돌아감
            }
        }
    }
}