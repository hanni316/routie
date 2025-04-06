package com.example.routie_wear.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

object VibrationUtil {
    fun vibrate(context: Context, durationMs: Long = 100) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMs)
        }
    }
}