package com.gbsb.routiemobile.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.FragmentMakingroutineBinding
import com.gbsb.routiemobile.dto.Exercise
import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.network.ExerciseAdapter
import com.gbsb.routiemobile.network.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakingroutineFragment : Fragment() {
    private var _binding: FragmentMakingroutineBinding? = null
    private val binding get() = _binding!!

    private val selectedExercises = mutableListOf<Exercise>()
    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakingroutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        exerciseAdapter = ExerciseAdapter(selectedExercises)
        binding.routineRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exerciseAdapter
        }

        // 운동 추가 버튼 클릭 시 다이얼로그 표시
        binding.addExerciseButton.setOnClickListener {
            showExerciseSelectionDialog()
        }

        binding.saveButton.setOnClickListener {
            createRoutine()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showExerciseSelectionDialog() {
        RetrofitClient.exerciseApi.getAllExercises().enqueue(object : Callback<List<Exercise>> {
            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                if (response.isSuccessful) {
                    val exerciseList = response.body() ?: emptyList()
                    showExerciseDialog(exerciseList)
                } else {
                    Toast.makeText(requireContext(), "운동 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "운동 목록 불러오기 실패: ${t.localizedMessage}")
            }
        })
    }

    private fun showExerciseDialog(exerciseList: List<Exercise>) {
        if (exerciseList.isEmpty()) {
            Toast.makeText(requireContext(), "운동 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val exerciseNames = exerciseList.map { it.name }.toTypedArray()
        var selectedIndex = -1

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("운동 선택")

        builder.setSingleChoiceItems(exerciseNames, -1) { _, which ->
            selectedIndex = which
        }

        builder.setPositiveButton("추가") { dialog, _ ->
            if (selectedIndex != -1) {
                val selectedExercise = exerciseList[selectedIndex]

                // 이미 추가된 운동인지 체크
                if (!selectedExercises.any { it.id == selectedExercise.id }) {
                    selectedExercises.add(selectedExercise)
                    exerciseAdapter.notifyItemInserted(selectedExercises.size - 1)
                    Toast.makeText(requireContext(), "${selectedExercise.name} 추가됨!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "이미 추가된 운동입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
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
                        Toast.makeText(requireContext(), "루틴이 성공적으로 저장되었습니다!", Toast.LENGTH_SHORT).show()
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
