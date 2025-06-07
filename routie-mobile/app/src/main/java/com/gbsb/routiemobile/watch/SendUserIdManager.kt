package com.gbsb.routiemobile.watch

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.*
import kotlinx.coroutines.tasks.await


object SendUserIdManager {
    suspend fun sendUserId(context: Context, userId: String) {
        val nodeClient = Wearable.getNodeClient(context)
        val nodes = nodeClient.connectedNodes.await()
        Log.d("SendUserId", "🔗 connected nodes count=${nodes.size} -> ${nodes.map { it.id to it.displayName }}")

        for (node in nodes) {
            try {
                Wearable.getMessageClient(context)
                    .sendMessage(node.id, "/user-id", userId.toByteArray())
                    .await()
                Log.d("SendUserId", "✔ userId 전송 성공 to ${node.displayName}: $userId")
            } catch (e: Exception) {
                Log.e("SendUserId", "✖ 전송 실패 to ${node.displayName}", e)
            }
        }
    }
}