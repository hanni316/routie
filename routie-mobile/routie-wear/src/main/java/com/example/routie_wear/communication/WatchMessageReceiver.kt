package com.example.routie_wear.communication

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchMessageReceiver(
    private val context: Context,
    private val onUserIdReceived: (String) -> Unit
) : MessageClient.OnMessageReceivedListener {

    init {
        Wearable.getMessageClient(context).addListener(this)
    }

    override fun onMessageReceived(event: MessageEvent) {
        if (event.path == "/user-id") {
            val userId = String(event.data)
            Log.d("WatchReceiver", "userId 수신됨: $userId")

            //SharedPreferences 등에 저장 가능
            CoroutineScope(Dispatchers.IO).launch {
                val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                prefs.edit().putString("userId", userId).apply()
            }

            onUserIdReceived(userId)
        }
    }

    fun unregister() {
        Wearable.getMessageClient(context).removeListener(this)
    }
}