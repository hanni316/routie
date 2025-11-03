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
import com.gbsb.routiemobile.dto.ExerciseCategory
import com.gbsb.routiemobile.adapter.ExerciseAdapter
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.tabs.TabLayout
import androidx.recyclerview.widget.RecyclerView

import androidx.core.content.ContextCompat
import android.graphics.Color



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

        // ToggleButton 선택 시 스타일 변경
        val toggleButtons = listOf(
            binding.sundayButton,
            binding.mondayButton,
            binding.tuesdayButton,
            binding.wednesdayButton,
            binding.thursdayButton,
            binding.fridayButton,
            binding.saturdayButton
        )
        val normalColor = ContextCompat.getColor(requireContext(), R.color.font_color)
        val selectedColor = Color.parseColor("#FFFFFF") //선택 시 흰색 글씨

        toggleButtons.forEach { button ->
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    button.setTextColor(selectedColor)   // 선택된 경우
                } else {
                    button.setTextColor(normalColor)     // 선택 해제된 경우
                }
            }
        }

        // RecyclerView 설정
        exerciseAdapter = ExerciseAdapter(selectedExercises){ /* 아무 동작 없음 */ }
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
        val dialogView = layoutInflater.inflate(
            R.layout.fragment_exercise,
            null, false
        )
        val tabCategories = dialogView.findViewById<TabLayout>(R.id.tabCategories)
        val rvExercises  = dialogView.findViewById<RecyclerView>(R.id.rvExercises)

        val dialogAdapter = ExerciseAdapter(mutableListOf()) { /* position만 넘기고 처리 X */ }

        rvExercises.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dialogAdapter
        }
        RetrofitClient.exerciseApi.getAllCategories().enqueue(object: Callback<List<ExerciseCategory>> {
            override fun onResponse(
                call: Call<List<ExerciseCategory>>,
                response: Response<List<ExerciseCategory>>
            ) {
                val categories = response.body() ?: emptyList()

                tabCategories.addTab(tabCategories.newTab().setText("전체"))
                categories.forEach { tabCategories.addTab(tabCategories.newTab().setText(it.name)) }

                tabCategories.getTabAt(0)?.select()
                loadExercisesForDialog(null, dialogAdapter)

                tabCategories.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {

                        val catId = if (tab.position == 0) {
                            null
                        } else {
                            categories[tab.position - 1].id
                        }
                        loadExercisesForDialog(catId, dialogAdapter)
                    }
                    override fun onTabUnselected(tab: TabLayout.Tab) {}
                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })
            }
            override fun onFailure(call: Call<List<ExerciseCategory>>, t: Throwable) {
                Toast.makeText(requireContext(), "카테고리 로딩 실패", Toast.LENGTH_SHORT).show()
            }
        })
        
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setNegativeButton("취소", null)
            .setPositiveButton("추가") { _, _ ->
                val chosen = dialogAdapter.getSelectedExercise()
                if (chosen == null) {
                    Toast.makeText(requireContext(), "운동을 하나 선택하세요.", Toast.LENGTH_SHORT).show()
                } else if (selectedExercises.any { it.id == chosen.id }) {
                    Toast.makeText(requireContext(), "이미 추가된 운동입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 1) 데이터에 추가
                    selectedExercises.add(chosen)
                    // 2) 화면에 반영
                    exerciseAdapter.notifyItemInserted(selectedExercises.size - 1)
                }
            }
            .show()
    }

    private fun loadExercisesForDialog(
        categoryId: Long?,
        dialogAdapter: ExerciseAdapter
    ) {
        val call = if (categoryId == null) {
            RetrofitClient.exerciseApi.getAllExercises()
        } else {
            RetrofitClient.exerciseApi.getExercisesByCategory(categoryId)
        }
        call.enqueue(object: Callback<List<Exercise>> {
            override fun onResponse(
                call: Call<List<Exercise>>,
                response: Response<List<Exercise>>
            ) {
                val list = response.body() ?: emptyList()
                // 실제로 리스트를 교체
                dialogAdapter.submitList(list)
            }
            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(requireContext(), "운동 로딩 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createRoutineWithExercises() {
        val routineName = binding.routineNameEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()

        val selectedDays = mutableListOf<String>()

        if (binding.sundayButton.isChecked) selectedDays.add("sunday")
        if (binding.mondayButton.isChecked) selectedDays.add("monday")
        if (binding.tuesdayButton.isChecked) selectedDays.add("tuesday")
        if (binding.wednesdayButton.isChecked) selectedDays.add("wednesday")
        if (binding.thursdayButton.isChecked) selectedDays.add("thursday")
        if (binding.fridayButton.isChecked) selectedDays.add("friday")
        if (binding.saturdayButton.isChecked) selectedDays.add("saturday")

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

        // SharedPreferences에서 userId 가져오기
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", 0)
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
