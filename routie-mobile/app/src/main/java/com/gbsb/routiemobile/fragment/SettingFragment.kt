package com.gbsb.routiemobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.FragmentSettingBinding
import com.gbsb.routiemobile.dto.User
import com.gbsb.routiemobile.dto.ProfileImageResponseDto
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileImageLauncher: ActivityResultLauncher<String>

    companion object {
        private const val SERVER_IP = "192.168.45.132" // ip 주소
    }

    private val profileImageCallback = ActivityResultCallback<Uri?> { uri ->
        uri?.let { selectedUri ->
            _binding?.let { safeBinding ->
                safeBinding.profileSetting.setImageURI(selectedUri)
                uploadProfileImage(selectedUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent(), profileImageCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfileImage()

        val navController = findNavController()

        binding.profileSetting.setOnClickListener {
            profileImageLauncher.launch("image/*")
        }
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        if (userId != null) {
            RetrofitClient.userApi.getUser(userId)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            binding.tvNickname.text = user?.name ?: "닉네임 없음"
                        } else {
                            binding.tvNickname.text = "닉네임 불러오기 실패"
                            Log.e("SettingFragment", "응답 실패: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        binding.tvNickname.text = "닉네임 불러오기 실패"
                        Log.e("SettingFragment", "네트워크 오류: ${t.message}")
                    }
                })
        }

        view.findViewById<ImageButton>(R.id.accountBtn).setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_accountFragment)
        }

        view.findViewById<ImageButton>(R.id.alarmSetBtn).setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_alarmFragment)
        }

        view.findViewById<ImageButton>(R.id.userInfoModBtn).setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_userInfoFragment)
        }

        view.findViewById<ImageButton>(R.id.logoutBtn).setOnClickListener {
            logoutUser()
            navController.navigate(R.id.action_settingFragment_to_loginFragment)
        }
    }

    private fun uploadProfileImage(uri: Uri) {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return
        val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", "profile.jpg", requestBody)

        val userId = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("userId", null) ?: return

        RetrofitClient.userApi.uploadProfileImage(userId, multipartBody)
            .enqueue(object : Callback<ProfileImageResponseDto> {
                override fun onResponse(
                    call: Call<ProfileImageResponseDto>,
                    response: Response<ProfileImageResponseDto>
                ) {
                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.imageUrl
                        Log.d("프로필 업로드", "성공: $imageUrl")
                        saveProfileImageUrl(imageUrl)
                        // loadProfileImage()는 생략: 재진입 시 자동 호출됨
                    } else {
                        Log.e("프로필 업로드", "실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ProfileImageResponseDto>, t: Throwable) {
                    Log.e("프로필 업로드", "에러 발생: ${t.message}")
                }
            })
    }

    private fun saveProfileImageUrl(imageUrl: String?) {
        imageUrl ?: return
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("profile_image_url", imageUrl).apply()
    }

    private fun loadProfileImage() {
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val imageUrl = prefs.getString("profile_image_url", null)

        if (!imageUrl.isNullOrBlank()) {
            val fullUrl = "http://$SERVER_IP:8080$imageUrl"
            Log.d("프로필 이미지 로드", "URL: $fullUrl")

            Glide.with(this)
                .load(fullUrl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile) // 로딩 실패 시 대체 이미지
                .into(binding.profileSetting)
        }
    }

    private fun logoutUser() {
        val sharedPreferences =
            requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("isLoggedIn")
            remove("userId")
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}