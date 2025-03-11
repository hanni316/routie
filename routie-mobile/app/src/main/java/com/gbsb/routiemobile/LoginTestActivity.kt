package com.gbsb.routiemobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.api.UserApiService
import com.gbsb.routiemobile.dto.LoginRequest
import com.gbsb.routiemobile.dto.LoginResponse
import com.gbsb.routiemobile.dto.SignupRequest
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginTestActivity : AppCompatActivity() {
    private lateinit var editTextLoginUserId: EditText
    private lateinit var editTextLoginPassword: EditText
    private lateinit var btnLogin: Button

    private lateinit var editTextSignupUserId: EditText
    private lateinit var editTextSignupEmail: EditText
    private lateinit var editTextSignupPassword: EditText
    private lateinit var editTextSignupName: EditText
    private lateinit var btnSignup: Button

    private val apiService: UserApiService by lazy {
        RetrofitClient.userApi
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logintest)

        // 로그인 UI 요소 초기화
        editTextLoginUserId = findViewById(R.id.editTextLoginUserId)
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // 회원가입 UI 요소 초기화
        editTextSignupUserId = findViewById(R.id.editTextSignupUserId)
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail)
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword)
        editTextSignupName = findViewById(R.id.editTextSignupName)
        btnSignup = findViewById(R.id.btnSignup)

        // 로그인 버튼 클릭 시
        btnLogin.setOnClickListener {
            val userId = editTextLoginUserId.text.toString()
            val password = editTextLoginPassword.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(userId, password)
                loginUser(loginRequest)
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 시
        btnSignup.setOnClickListener {
            val userId = editTextSignupUserId.text.toString()
            val email = editTextSignupEmail.text.toString()
            val password = editTextSignupPassword.text.toString()
            val name = editTextSignupName.text.toString()

            if (userId.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                val signupRequest = SignupRequest(userId, email, password, name)
                registerUser(signupRequest)
            } else {
                Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 로그인 기능
    private fun loginUser(request: LoginRequest){
        apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("API_SUCCESS", "로그인 성공: ${loginResponse?.userId}")
                    val intent = Intent(this@LoginTestActivity, MainActivity::class.java)
                    intent.putExtra("userId", loginResponse?.userId)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginTestActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginTestActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 회원가입 기능
    private fun registerUser(request: SignupRequest) {
        apiService.registerUser(request).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val message =
                        body?.get("message") ?: "Signup Successful" // JSON 응답에서 메시지 가져오기
                    Toast.makeText(this@LoginTestActivity, message, Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Signup Failed"
                    Log.e("API_ERROR", "회원가입 실패: $errorMessage") // 오류 메시지 확인
                    Toast.makeText(
                        this@LoginTestActivity,
                        "회원가입 실패: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("NETWORK_ERROR", "네트워크 오류 발생: ${t.message}") // 네트워크 오류 로그
                Toast.makeText(
                    this@LoginTestActivity,
                    "네트워크 오류 발생: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
