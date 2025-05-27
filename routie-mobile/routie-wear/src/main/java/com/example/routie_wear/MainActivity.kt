package com.example.routie_wear

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.compose.material.MaterialTheme
import com.example.routie_wear.ui.theme.AppTheme
import com.example.routie_wear.viewmodel.RoutineViewModel
import com.example.routie_wear.ui.MainNav
import com.example.routie_wear.communication.WatchMessageReceiver

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: RoutineViewModel
    private lateinit var receiver: WatchMessageReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = RoutineViewModel()

        //userId 수신 및 ViewModel에 전달
        receiver = WatchMessageReceiver(this) { userId ->
            Log.d("MainActivity", "userId 수신: $userId")
            viewModel.userId = userId
        }

        setContent {
            AppTheme {
                val viewModel: RoutineViewModel = viewModel
                MainNav(viewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.unregister()
    }
}