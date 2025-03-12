package com.gbsb.routiemobile

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.dto.LoginRequest
import com.gbsb.routiemobile.dto.LoginResponse
import com.gbsb.routiemobile.network.RetrofitClient
import com.gbsb.routiemobile.fragment.SignupFragment
import com.gbsb.routiemobile.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // 로그인 화면 XML 연결

        sharedPrefManager = SharedPrefManager(this)

        // EditText & 버튼 찾기
        val editTextUserId = findViewById<EditText>(R.id.editTextTextID)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<ImageButton>(R.id.imageButton)
        val btnSignup = findViewById<ImageButton>(R.id.btn_signup)

        // 회원가입 버튼 클릭 시 회원가입 화면으로 이동
        btnSignup.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignupFragment()) // SignupFragment를 fragment_container에 추가
                .addToBackStack(null) // 뒤로 가기 버튼으로 로그인 화면으로 돌아올 수 있도록 추가
                .commit()
        }

        // 로그인 버튼 클릭 시 Retrofit으로 로그인 요청
        btnLogin.setOnClickListener {
            val userId = editTextUserId.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(userId, password)

            RetrofitClient.userApi.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()

                        // ✅ 로그인 성공 시 SharedPreferences에 userId 저장
                        sharedPrefManager.saveLoginInfo(loginResponse?.userId ?: "")

                        Toast.makeText(this@LoginActivity, "로그인 성공! ${loginResponse?.name}님 환영합니다.", Toast.LENGTH_SHORT).show()

                        // ✅ 로그인 성공 후 홈 화면(MainActivity)으로 이동
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userId", loginResponse?.userId) // 로그인한 사용자 정보 전달
                        startActivity(intent)
                        finish() // 로그인 화면 종료
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패: 아이디 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "네트워크 오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
