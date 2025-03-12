package com.gbsb.routiemobile.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gbsb.routiemobile.LoginActivity
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.api.UserApiService
import com.gbsb.routiemobile.dto.SignupRequest
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment() {
    private lateinit var editTextUserId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignup: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // UI 요소 연결
        editTextUserId = view.findViewById(R.id.id_)
        editTextPassword = view.findViewById(R.id.password_)
        btnSignup = view.findViewById(R.id.imageButton3)

        // 회원가입 버튼 클릭 이벤트
        btnSignup.setOnClickListener {
            val userId = editTextUserId.text.toString()
            val password = editTextPassword.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val signupRequest = SignupRequest(userId, password, userId , userId) // 이메일을 userId와 동일하게 설정
                registerUser(signupRequest)
            } else {
                Toast.makeText(requireContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun registerUser(request: SignupRequest) {
        val apiService = RetrofitClient.userApi

        apiService.registerUser(request).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "회원가입 성공! 로그인해주세요.", Toast.LENGTH_SHORT).show()

                    // ✅ 회원가입 성공 후 LoginActivity로 이동
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
