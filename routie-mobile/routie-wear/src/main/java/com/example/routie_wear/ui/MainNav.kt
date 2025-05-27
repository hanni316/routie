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
                navController.navigate("routine")
            }
        }

        composable("routine") {
            RoutineListScreen(viewModel) {
                navController.navigate("workouts")
            }
        }

        composable("timer") {
            WorkoutTimerScreen(viewModel) {
                navController.navigate("workouts")
            }
        }
        composable("workouts") {
            WorkoutListScreen(
                viewModel,
                onWorkoutSelected = { navController.navigate("timer") },
                onRoutineFinished = {
                    navController.popBackStack("routine", inclusive = false)
                }
            )
        }
    }
}