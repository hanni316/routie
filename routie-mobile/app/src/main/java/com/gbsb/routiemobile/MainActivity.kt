package com.gbsb.routiemobile

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.gbsb.routiemobile.fragment.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ✅ MainFragment를 추가
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.MainFragment, MainFragment())
                .commit()
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        // ✅ 로그인 여부 체크 후 첫 화면 설정
        checkLoginState()

        // ✅ 시스템 바 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }
    private fun checkLoginState() { //PreferenxeManager -> getSharedPreferences로 변경
        val sharedPreferences = getSharedPreferences("app_prefs", 0)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            navController.navigate(R.id.LoginFragment) // 로그인 화면으로 이동
        } else {
            navController.navigate(R.id.MainFragment) // 메인 화면으로 이동
        }
    }

}