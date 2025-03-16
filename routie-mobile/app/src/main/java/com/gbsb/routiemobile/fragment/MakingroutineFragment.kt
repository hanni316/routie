package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.FragmentMakingroutineBinding
import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.network.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakingroutineFragment : Fragment() {
    private var _binding: FragmentMakingroutineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakingroutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 저장 버튼 클릭 시 루틴 생성 API 호출
        binding.root.findViewById<ImageButton>(R.id.save_button).setOnClickListener {
            createRoutine()
        }
    }

    private fun createRoutine() {
        val routineName = binding.routineNameEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()

        if (routineName.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "루틴 이름과 설명을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val newRoutine = Routine(
            name = routineName,
            description = description,
            duration = 30, // 기본값 (운동 시간)
            caloriesBurned = 200 // 기본값 (소모 칼로리)
        )

        // ✅ SharedPreferences에서 userId 가져오기
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", 0)
        val userId = sharedPreferences.getString("userId", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "로그인 정보가 없습니다. 다시 로그인하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // json 변환 로그 출력
        val requestJson = Gson().toJson(newRoutine)
        Log.d("API_REQUEST", "보내는 데이터: $requestJson")

        RetrofitClient.routineApi.createRoutine(userId, newRoutine)
            .enqueue(object : Callback<Routine> {
                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "루틴이 성공적으로 저장되었습니다!", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("API_SUCCESS", "루틴 생성 성공: ${response.body()}")
                    } else {
                        Toast.makeText(requireContext(), "루틴 저장 실패", Toast.LENGTH_SHORT).show()
                        Log.e("API_ERROR", "루틴 생성 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Routine>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                    Log.e("API_FAILURE", "네트워크 오류: ${t.localizedMessage}")
                }
            })
    }
}