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
import com.gbsb.routiemobile.databinding.FragmentUserInfoBinding
import com.gbsb.routiemobile.dto.PasswordUpdateRequest
import com.gbsb.routiemobile.dto.User
import com.gbsb.routiemobile.dto.UserUpdateRequest
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private val apiService: UserApiService by lazy {
        RetrofitClient.userApi
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", 0)
        val userId = sharedPreferences.getString("userId", null)

        binding.btnModify.setOnClickListener {
            val nickname = binding.editTextnickname2.text.toString()
            val height = binding.editTextHeight2.text.toString().toIntOrNull() ?: 0
            val weight = binding.editTextWeight2.text.toString().toIntOrNull() ?: 0

            if (userId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 유저 정보 업데이트 요청
            val updateRequest = UserUpdateRequest(nickname, height, weight)
            apiService.updateUser(userId, updateRequest).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "정보 수정 완료", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })

            // 비밀번호 변경
            val currentPw = binding.editTextPassword2.text.toString()
            val newPw = binding.editTextPassword3.text.toString()

            if (newPw.isNotEmpty()) {
                if (currentPw.isEmpty()) {
                    Toast.makeText(requireContext(), "기존 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val pwRequest = PasswordUpdateRequest(currentPw, newPw)
                apiService.updatePassword(userId, pwRequest).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.btnCancel.setOnClickListener {
            binding.editTextHeight2.setText("")
            binding.editTextWeight2.setText("")
            binding.editTextnickname2.setText("")
            binding.editTextPassword2.setText("")
            binding.editTextPassword3.setText("")
            Toast.makeText(requireContext(), "변경 취소됨", Toast.LENGTH_SHORT).show()
        }

        // 회원 탈퇴
        binding.imageButton2.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("app_prefs", 0)
            val userId = sharedPreferences.getString("userId", null)

            if (userId.isNullOrEmpty()) return@setOnClickListener
            apiService.deleteUser(userId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "회원탈퇴 완료", Toast.LENGTH_SHORT).show()

                        prefs.edit().clear().apply()
                        findNavController().navigate(R.id.action_userInfoFragment_to_loginFragment)
                    } else {
                        Toast.makeText(requireContext(), "회원탈퇴 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}