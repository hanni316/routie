package com.gbsb.routiemobile.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.FragmentMakingroutineBinding
import com.gbsb.routiemobile.dto.Exercise
import com.gbsb.routiemobile.dto.ExerciseRequest
import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.dto.RoutineRequest
import com.gbsb.routiemobile.adapter.ExerciseAdapter
import com.gbsb.routiemobile.network.RetrofitClient
import android.graphics.Color
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

        // ✅ ToggleButton 선택 시 스타일 변경
        val toggleButtons = listOf(
            binding.sundayButton,
            binding.mondayButton,
            binding.tuesdayButton,
            binding.wednesdayButton,
            binding.thursdayButton,
            binding.fridayButton,
            binding.saturdayButton
        )

        for (button in toggleButtons) {
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    button.setBackgroundColor(Color.parseColor("#B08968"))  // 선택된 배경
                    button.setTextColor(Color.WHITE)
                } else {
                    button.setBackgroundColor(Color.parseColor("#F6D6D6"))  // 기본 배경
                    button.setTextColor(Color.BLACK)
                }
            }

            // ✅ 초기화 시 상태 반영
            if (button.isChecked) {
                button.setBackgroundColor(Color.parseColor("#B08968"))
                button.setTextColor(Color.WHITE)
            } else {
                button.setBackgroundColor(Color.parseColor("#F6D6D6"))
                button.setTextColor(Color.BLACK)
            }
        }
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

        // 루틴 저장 버튼 클릭 시 루틴 + 운동 함께 저장
        binding.createButton.setOnClickListener {
            createRoutineWithExercises()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var cachedExerciseList: List<Exercise>? = null

    private fun showExerciseSelectionDialog() {
        if (cachedExerciseList != null) {
            showExerciseDialog(cachedExerciseList!!)
        } else {
            RetrofitClient.exerciseApi.getAllExercises().enqueue(object : Callback<List<Exercise>> {
                override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
                    if (response.isSuccessful) {
                        cachedExerciseList = response.body() ?: emptyList()
                        showExerciseDialog(cachedExerciseList!!)
                    } else {
                        Toast.makeText(requireContext(), "운동 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                }
            })
        }
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

    private fun createRoutineWithExercises() {
        val routineName = binding.routineNameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()

        val selectedDays = mutableListOf<String>()

        if (binding.sundayButton.isChecked) selectedDays.add("일")
        if (binding.mondayButton.isChecked) selectedDays.add("월")
        if (binding.tuesdayButton.isChecked) selectedDays.add("화")
        if (binding.wednesdayButton.isChecked) selectedDays.add("수")
        if (binding.thursdayButton.isChecked) selectedDays.add("목")
        if (binding.fridayButton.isChecked) selectedDays.add("금")
        if (binding.saturdayButton.isChecked) selectedDays.add("토")

        if (selectedExercises.isEmpty()) {
            Toast.makeText(requireContext(), "운동을 최소 1개 이상 추가해야 합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (routineName.isEmpty()) {
            Toast.makeText(requireContext(), "루틴 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 선택된 운동 목록을 ExerciseRequest 리스트로 변환
        val exerciseRequestList = selectedExercises.map {
            ExerciseRequest(it.id, 30) //일단 duration값 : 30
        }

        // RoutineRequest 생성 (루틴 + 운동 리스트)
        val routineRequest = RoutineRequest(
            name = routineName,
            description = description,
            days = selectedDays,
            exercises = exerciseRequestList
        )

        // ✅ SharedPreferences에서 userId 가져오기
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", 0)
        val userId = sharedPreferences.getString("userId", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "로그인 정보가 없습니다. 다시 로그인하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 서버에 루틴과 운동 함께 저장 요청
        RetrofitClient.routineApi.createRoutineWithExercises(userId, routineRequest)
            .enqueue(object : Callback<Routine> {
                override fun onResponse(call: Call<Routine>, response: Response<Routine>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "루틴이 성공적으로 저장되었습니다!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.RoutineFragment)
                    } else {
                        Toast.makeText(requireContext(), "루틴 저장 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Routine>, t: Throwable) {
                    Log.e("RoutineApi", "네트워크 오류: ${t.message}")
                    Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
