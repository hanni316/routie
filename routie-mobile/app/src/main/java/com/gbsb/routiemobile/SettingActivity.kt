package com.gbsb.routiemobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.utils.SharedPrefManager
import android.util.Log

class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPrefManager = SharedPrefManager(this)

        val btnLogout = findViewById<ImageButton>(R.id.logoutButton) // 로그아웃 버튼


        // ✅ 로그아웃 버튼 클릭 이벤트
        btnLogout.setOnClickListener {
            Log.d("SettingActivity", "✅ 로그아웃 버튼 클릭됨")

            sharedPrefManager.logout() // ✅ 세션 삭제
            Log.d("SettingActivity", "✅ 로그인 정보 삭제 완료")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // ✅ 현재 액티비티 종료 (뒤로 가기 방지)
        }
    }
}
