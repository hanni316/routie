package com.example.routie_wear.communication

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

class WatchMessageReceiver(
    private val context: Context,
    private val onUserIdReceived: (String) -> Unit
) : MessageClient.OnMessageReceivedListener {

    init {
        Wearable.getMessageClient(context).addListener(this)
        Log.d("WatchMessageReceiver", "메시지 수신 대기 시작")
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/user-id") {
            val userId = String(messageEvent.data)
            Log.d("WatchMessageReceiver", "userId 수신: $userId")

            // 저장
            context.getSharedPreferences("wear_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("userId", userId)
                .apply()

            // ViewModel 등으로 전달
            onUserIdReceived(userId)
        }
    }

    fun unregister() {
        Wearable.getMessageClient(context).removeListener(this)
        Log.d("WatchMessageReceiver", "메시지 리스너 해제")
    }
}