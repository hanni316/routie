package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.ExerciseResponseAdapter
import com.gbsb.routiemobile.dto.ExerciseCategory
import com.gbsb.routiemobile.dto.ExerciseResponse
import com.gbsb.routiemobile.dto.RoutineResponse
import com.gbsb.routiemobile.dto.RoutineUpdateRequest
import com.gbsb.routiemobile.dto.Exercise
import com.gbsb.routiemobile.network.RetrofitClient
import com.google.android.material.tabs.TabLayout
import com.gbsb.routiemobile.adapter.ExerciseAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyroutineFragment : Fragment() {

    private var routineId: Long = 0L

    private lateinit var routineNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: ImageButton
    private lateinit var addExerciseButton: ImageButton

    private lateinit var dayButtons: Map<String, ToggleButton>
    private val selectedDays = mutableListOf<String>()

    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseResponseAdapter
    private val exerciseList = mutableListOf<ExerciseResponse>()

    // 서버에 아직 저장되지 않은 항목
    private val exercisesToAdd = mutableListOf<Exercise>()
    // 서버에 저장된 항목
    private val exercisesToRemove = mutableListOf<ExerciseResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        routineId = arguments?.getLong("routineId") ?: 0L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modifyroutine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayButtons = mapOf(
            "sunday" to view.findViewById(R.id.sundayButton),
            "monday" to view.findViewById(R.id.mondayButton),
            "tuesday" to view.findViewById(R.id.tuesdayButton),
            "wednesday" to view.findViewById(R.id.wednesdayButton),
            "thursday" to view.findViewById(R.id.thursdayButton),
            "friday" to view.findViewById(R.id.fridayButton),
            "saturday" to view.findViewById(R.id.saturdayButton),
        )

        routineNameEditText = view.findViewById(R.id.routineNameEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        saveButton = view.findViewById(R.id.save_button)
        addExerciseButton = view.findViewById(R.id.addExerciseButton)

        exerciseRecyclerView = view.findViewById(R.id.routineRecyclerView)
        exerciseAdapter = ExerciseResponseAdapter(exerciseList) { exercise ->
            deleteExercise(exercise)
        }
        exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exerciseRecyclerView.adapter = exerciseAdapter

        loadExerciseList()
        loadRoutineDetail()

        Log.d("ModifyRoutine", "Loading exercises for routineId=$routineId")

        saveButton.setOnClickListener {
            updateRoutine()
        }

        addExerciseButton.setOnClickListener {
            showExerciseSelectionDialog()
        }
    }

    private fun loadExerciseList() {
        RetrofitClient.routineApi.getExercisesByRoutine(routineId.toString())
            .enqueue(object : Callback<List<ExerciseResponse>> {
                override fun onResponse(
                    call: Call<List<ExerciseResponse>>,
                    response: Response<List<ExerciseResponse>>
                ) {
                    if (response.isSuccessful) {
                        exerciseList.clear()
                        response.body()?.let { exerciseList.addAll(it) }
                        exerciseAdapter.notifyDataSetChanged()
                    } else {
                        context?.let {
                            Toast.makeText(it, "운동 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<List<ExerciseResponse>>, t: Throwable) {
                    context?.let {
                        Toast.makeText(it, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun loadRoutineDetail() {
        RetrofitClient.routineApi.getRoutineDetail(routineId.toString())
            .enqueue(object : Callback<RoutineResponse> {
                override fun onResponse(
                    call: Call<RoutineResponse>,
                    response: Response<RoutineResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { routine ->
                            routineNameEditText.setText(routine.name)
                            descriptionEditText.setText(routine.description)

                            selectedDays.clear()
                            routine.days?.let { selectedDays.addAll(it) }
                            updateDayToggleUI()
                        }
                    } else {
                        context?.let {
                            Toast.makeText(it, "루틴 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<RoutineResponse>, t: Throwable) {
                    context?.let {
                        Toast.makeText(it, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun updateDayToggleUI() {
        for ((day, button) in dayButtons) {
            button.isChecked = selectedDays.contains(day)
        }
    }

    private fun collectSelectedDays() {
        selectedDays.clear()
        for ((day, button) in dayButtons) {
            if (button.isChecked) {
                selectedDays.add(day)
            }
        }
    }

    private fun updateRoutine() {
        val newName = routineNameEditText.text.toString().trim()
        val newDescription = descriptionEditText.text.toString().trim()

        if (newName.isEmpty()) {
            context?.let {
                Toast.makeText(it, "루틴 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            return
        }

        collectSelectedDays()

        if (selectedDays.isEmpty()) {
            Toast.makeText(requireContext(), "날짜를 선택하세요.", Toast.LENGTH_SHORT).show()
            return
        }


        val routineUpdateRequest = RoutineUpdateRequest(name = newName, description = newDescription, days = selectedDays)

        RetrofitClient.routineApi.updateRoutine(routineId.toString(), routineUpdateRequest)
            .enqueue(object : Callback<RoutineResponse> {
                override fun onResponse(
                    call: Call<RoutineResponse>,
                    response: Response<RoutineResponse>
                ) {
                    if (response.isSuccessful) {
                        processExerciseModifications()
                        context?.let {
                            Toast.makeText(it, "루틴이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        findNavController().popBackStack()
                    } else {
                        context?.let {
                            Toast.makeText(it, "루틴 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<RoutineResponse>, t: Throwable) {
                    context?.let {
                        Toast.makeText(it, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun processExerciseModifications() {
        // 추가된 운동 처리
        for (exercise in exercisesToAdd) {
            RetrofitClient.routineApi.addExerciseToRoutine(routineId.toString(), exercise.id.toString())
                .enqueue(object : Callback<ExerciseResponse> {
                    override fun onResponse(
                        call: Call<ExerciseResponse>,
                        response: Response<ExerciseResponse>
                    ) {
                        if (!response.isSuccessful) {
                            context?.let {
                                Toast.makeText(it, "운동 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
                        context?.let {
                            Toast.makeText(it, "네트워크 오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

        for (exercise in exercisesToRemove) {
            if (exercise.routineExerciseId > 0L) {
                RetrofitClient.routineApi.removeExerciseFromRoutine(exercise.routineExerciseId)
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (!response.isSuccessful) {
                                context?.let {
                                    Toast.makeText(it, "운동 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            context?.let {
                                Toast.makeText(it, "네트워크 오류", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }
        }
    }


    private fun deleteExercise(exercise: ExerciseResponse) {
        // UI에서 즉시 항목 제거
        val index = exerciseList.indexOf(exercise)
        if (index >= 0) {
            exerciseList.removeAt(index)
            exerciseAdapter.notifyItemRemoved(index)
        }

        // 서버에 저장된 운동
        if (exercise.routineExerciseId > 0L) {
            exercisesToRemove.add(exercise)
        } else {
            exercisesToAdd.removeAll { it.name == exercise.exerciseName }
        }

    }


    // 운동 추가
    private fun showExerciseSelectionDialog() {

        val dialogView = layoutInflater.inflate(R.layout.fragment_exercise, null, false)
        val tabCategories = dialogView.findViewById<TabLayout>(R.id.tabCategories)
        val rvExercises  = dialogView.findViewById<RecyclerView>(R.id.rvExercises)

        val dialogAdapter = ExerciseAdapter(mutableListOf()) { selectedExercise ->

        }
        rvExercises.layoutManager = LinearLayoutManager(requireContext())
        rvExercises.adapter = dialogAdapter

        // 카테고리 불러오기 & 탭 세팅
        RetrofitClient.exerciseApi.getAllCategories().enqueue(object: Callback<List<ExerciseCategory>> {
            override fun onResponse(call: Call<List<ExerciseCategory>>, resp: Response<List<ExerciseCategory>>) {
                val cats = resp.body().orEmpty()
                tabCategories.addTab(tabCategories.newTab().setText("전체"))
                cats.forEach { tabCategories.addTab(tabCategories.newTab().setText(it.name)) }

                tabCategories.getTabAt(0)?.select()
                loadExercisesForDialog(null, dialogAdapter)

                tabCategories.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        val catId = if (tab.position == 0) null else cats[tab.position - 1].id
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

        // 다이얼로그
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setNegativeButton("취소", null)
            .setPositiveButton("추가") { _, _ ->
                val chosen = dialogAdapter.getSelectedExercise()
                if (chosen == null) {
                    Toast.makeText(requireContext(), "운동을 하나 선택하세요.", Toast.LENGTH_SHORT).show()
                } else if (exercisesToAdd.any { it.id == chosen.id }) {
                    Toast.makeText(requireContext(), "이미 추가된 운동입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    exercisesToAdd.add(chosen)
                    val newResp = ExerciseResponse(0L, chosen.name,chosen.enName)
                    exerciseList.add(newResp)
                    exerciseAdapter.notifyItemInserted(exerciseList.size - 1)
                }
            }
            .show()
    }

    private fun loadExercisesForDialog(
        categoryId: Long?,
        dialogAdapter: ExerciseAdapter
    ) {
        val call = categoryId?.let {
            RetrofitClient.exerciseApi.getExercisesByCategory(it)
        } ?: RetrofitClient.exerciseApi.getAllExercises()

        call.enqueue(object: Callback<List<Exercise>> {
            override fun onResponse(c: Call<List<Exercise>>, r: Response<List<Exercise>>) {
                dialogAdapter.submitList(r.body().orEmpty())
            }
            override fun onFailure(c: Call<List<Exercise>>, t: Throwable) {
                Toast.makeText(requireContext(), "운동 로딩 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
