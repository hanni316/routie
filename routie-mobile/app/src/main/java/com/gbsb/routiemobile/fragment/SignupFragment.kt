package com.gbsb.routiemobile.fragment

import android.os.Bundle
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
            val email = binding.editTextID.text.toString() //data 아이디로 넘어가게 임시 설정
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextID.text.toString() //마찬가지

            if (userId.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                val signupRequest = SignupRequest(userId, email, password, name)
                registerUser(signupRequest)
            } else {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(request: SignupRequest) {
        apiService.registerUser(request).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "회원가입 성공! 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment) // 로그인 화면으로 이동
                } else {
                    Toast.makeText(requireContext(), "회원가입 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}