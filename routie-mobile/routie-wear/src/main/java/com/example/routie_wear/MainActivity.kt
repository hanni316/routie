package com.example.routie_wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routie_wear.ui.theme.AppTheme
import com.example.routie_wear.viewmodel.RoutineViewModel
import com.example.routie_wear.ui.MainNav

class MainActivity : ComponentActivity() {
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val viewModel : RoutineViewModel = viewModel()
                MainNav(viewModel = viewModel)
            }
        }
    }


}