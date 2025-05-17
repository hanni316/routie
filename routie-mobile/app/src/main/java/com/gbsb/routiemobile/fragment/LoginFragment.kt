package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.api.UserApiService
import com.gbsb.routiemobile.databinding.FragmentLoginBinding
import com.gbsb.routiemobile.dto.LoginRequest
import com.gbsb.routiemobile.dto.LoginResponse
import com.gbsb.routiemobile.network.RetrofitClient
import com.gbsb.routiemobile.watch.SendUserIdManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val apiService: UserApiService by lazy {
        RetrofitClient.userApi
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  로그인 상태 확인 후 자동 로그인
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", 0)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            findNavController().navigate(R.id.MainFragment) // 자동 로그인 후 바로 이동
            return
        }

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {
            val userId = binding.editTextID.text.toString() ?: ""
            val password = binding.editTextPassword.text.toString() ?: ""

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(userId, password)
                loginUser(loginRequest)
            } else {
                Toast.makeText(requireContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 시
        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.SignupFragment)
        }
    }

    private fun loginUser(request: LoginRequest) {
        apiService.loginUser(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Toast.makeText(
                        requireContext(),
                        "환영합니다~ ID: ${loginResponse?.userId}",
                        Toast.LENGTH_SHORT
                    ).show()

                    //로그인 시 userId 워치로 송신
                    lifecycleScope.launch {
                        loginResponse?.userId?.let { userId ->
                            SendUserIdManager.sendUserId(requireContext(), userId)
                        }
                    }


                    val prefs = requireContext()
                        .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(prefs.edit()) {
                        putBoolean("isLoggedIn", true)
                        putString("userId", loginResponse?.userId)
                        putInt("goldAmount", loginResponse?.gold ?: 0)
                        putString("profile_image_url", loginResponse?.profileImageUrl)
                        apply()
                    }
                    Log.d("LoginFragment", "SharedPrefs 저장된 goldAmount=${prefs.getInt("goldAmount", -1)}")

                    // 로그인 성공 후 MainFragment로 이동
                    findNavController().navigate(R.id.MainFragment)
                } else {
                    Toast.makeText(requireContext(), "아이디와 비밀번호가 잘못되었습니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}