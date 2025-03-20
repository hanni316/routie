package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.api.UserApiService
import com.gbsb.routiemobile.databinding.FragmentSignupBinding
import com.gbsb.routiemobile.dto.SignupRequest
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val apiService: UserApiService by lazy {
        RetrofitClient.userApi
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 회원가입 버튼 클릭 시 서버에 회원가입 요청
        binding.btnSignup.setOnClickListener {
            val userId = binding.editTextID.text.toString()
            val email = binding.editTextPemail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextnickname.text.toString()
            val gender = binding.editTextGender.text.toString()
            val age = binding.editTextAge.text.toString().toIntOrNull() ?: 0
            val height = binding.editTextHeight.text.toString().toIntOrNull() ?: 0
            val weight = binding.editTextWeight.text.toString().toIntOrNull() ?: 0

            if (userId.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                val signupRequest = SignupRequest(userId, email, password, name, gender, age, height, weight)
                registerUser(signupRequest)
            } else {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(request: SignupRequest) {
        Log.d("API_REQUEST", "회원가입 요청 데이터: $request")

        apiService.registerUser(request).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                Log.d("API_RESPONSE", "회원가입 응답 코드: ${response.code()}")
                Log.d("API_RESPONSE", "회원가입 응답 바디: ${response.body()}")

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "회원가입 성공! 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment) // 로그인 화면으로 이동
                } else {
                    Toast.makeText(requireContext(), "회원가입 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "회원가입 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                Log.e("API_FAILURE", "회원가입 네트워크 오류: ${t.localizedMessage}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}