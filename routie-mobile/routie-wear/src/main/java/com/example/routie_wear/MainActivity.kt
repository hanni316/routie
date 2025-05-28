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
    private val viewModel by lazy { RoutineViewModel() }
    private lateinit var receiver: WatchMessageReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 메시지 수신 리스너 등록
        receiver = WatchMessageReceiver(this) { userId ->
            Log.d("MainActivity", "ViewModel에 userId 저장: $userId")
            viewModel.userId = userId
        }

        setContent {
            AppTheme {
                MainNav(viewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.unregister()
    }
}
