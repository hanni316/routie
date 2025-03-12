package com.gbsb.routiemobile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.R.id.logoutButton

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        val logoutButton = findViewById<ImageButton>(logoutButton)


        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) // 클래스명 수정
            startActivity(intent)
        }
    }

}