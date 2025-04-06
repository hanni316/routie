package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

        // ID 중복 확인 + 간격 조절
        binding.editTextID.addTextChangedListener {
            val inputId = it.toString()
            val layoutParams = binding.textViewIdCheck.layoutParams

            if (inputId.isNotBlank()) {
                apiService.checkUserId(inputId).enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful) {
                            val isAvailable = response.body() ?: false

                            // 공간 확보
                            layoutParams.height = LayoutParams.WRAP_CONTENT
                            binding.textViewIdCheck.layoutParams = layoutParams
                            binding.textViewIdCheck.visibility = View.VISIBLE

                            binding.textViewIdCheck.text = if (isAvailable) {
                                binding.textViewIdCheck.setTextColor(
                                    resources.getColor(android.R.color.holo_green_dark, null)
                                )
                                "사용할 수 있는 아이디입니다"
                            } else {
                                binding.textViewIdCheck.setTextColor(
                                    resources.getColor(android.R.color.holo_red_dark, null)
                                )
                                "이미 사용 중인 아이디입니다"
                            }
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        layoutParams.height = LayoutParams.WRAP_CONTENT
                        binding.textViewIdCheck.layoutParams = layoutParams
                        binding.textViewIdCheck.visibility = View.VISIBLE
                        binding.textViewIdCheck.text = "아이디 확인 실패"
                        binding.textViewIdCheck.setTextColor(
                            resources.getColor(android.R.color.holo_red_dark, null)
                        )
                    }
                })
            } else {
                // ID 없으면 공간 숨김
                layoutParams.height = 0
                binding.textViewIdCheck.layoutParams = layoutParams
                binding.textViewIdCheck.visibility = View.GONE
                binding.textViewIdCheck.text = ""
            }
        }

        // 회원가입 버튼 클릭
        binding.btnSignup.setOnClickListener {
            val userId = binding.editTextID.text.toString()
            val email = binding.editTextPemail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextnickname.text.toString()
            val gender = binding.editTextGender.text.toString()
            val age = binding.editTextAge.text.toString().toIntOrNull() ?: 0
            val height = binding.editTextHeight.text.toString().toIntOrNull() ?: 0
            val weight = binding.editTextWeight.text.toString().toIntOrNull() ?: 0

            val emailRegex = Regex("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")
            val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,20}$")

            if (userId.isEmpty() || email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(requireContext(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.matches(emailRegex)) {
                Toast.makeText(requireContext(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!password.matches(passwordRegex)) {
                Toast.makeText(requireContext(), "비밀번호는 8~20자, 영문자+숫자+특수문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val signupRequest = SignupRequest(userId, email, password, name, gender, age, height, weight)
            registerUser(signupRequest)
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
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
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
