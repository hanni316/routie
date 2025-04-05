package com.gbsb.routiemobile.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import android.graphics.Color
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.RoutineAdapter
import com.gbsb.routiemobile.adapter.RoutineLogAdapter
import com.gbsb.routiemobile.adapter.WeekDayAdapter
import com.gbsb.routiemobile.databinding.FragmentMainBinding
import com.gbsb.routiemobile.dto.ExerciseResponse
import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.dto.RoutineLog
import com.gbsb.routiemobile.dto.WeekDay
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

class MainFragment : Fragment() {

    private var isNoticeBubbleVisible = false
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var weekDayAdapter: WeekDayAdapter
    private lateinit var exerciseNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.txtNowdate.text = "$year 년 $month 월"

        // 날짜 선택 시 루틴 로그 조회
        weekDayAdapter = WeekDayAdapter(emptyList()) { newlySelectedDate ->
            selectedDate = newlySelectedDate
            showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)

            val userId = getUserIdFromPrefs()
            userId?.let {
                fetchRoutineLogs(it, selectedDate.toString())
                loadScheduledRoutines(it, selectedDate.toString())
            }
        }

        binding.recyclerWeekDays.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = weekDayAdapter
        }

        binding.btnSelectdate.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), { _, y, m, d ->
                binding.txtNowdate.text = "$y 년 ${m + 1} 월"
                val cal = Calendar.getInstance()
                cal.set(y, m, d)
                selectedDate = LocalDate.of(y, m + 1, d)
                showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)

                val userId = getUserIdFromPrefs()
                userId?.let {
                    fetchRoutineLogs(it, selectedDate.toString())
                }
            }, year, month - 1, day)

            val dayPickerId = resources.getIdentifier("day", "id", "android")
            dialog.datePicker.findViewById<View>(dayPickerId)?.visibility = View.GONE
            dialog.show()
        }

        exerciseNameTextView = binding.tvExerciseNames

        val userId = getUserIdFromPrefs()
        userId?.let {
            fetchRoutineLogs(it, selectedDate.toString())
            loadScheduledRoutines(it, selectedDate.toString())
        }

        binding.btnBell.setOnClickListener {
            binding.imgNoticefield.visibility =
                if (isNoticeBubbleVisible) View.GONE else View.VISIBLE
            isNoticeBubbleVisible = !isNoticeBubbleVisible
        }

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.SettingFragment)
        }

        binding.imgSketchbookStaricon.setOnClickListener {
            findNavController().navigate(R.id.RoutineFragment)
        }

        binding.btnShop.setOnClickListener {
            findNavController().navigate(R.id.StoreFragment)
        }

        // 초기 표시
        showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)

        // 앱 처음 진입 시 오늘 날짜 기준 루틴 로그 표시
        getUserIdFromPrefs()?.let {
            fetchRoutineLogs(it, selectedDate.toString())
        }

        val samsungHealthManager = com.gbsb.routiemobile.health.SamsungHealthManager(requireContext())

        samsungHealthManager.fetchStepsAndCalories(
            onResult = { steps, calories ->
                Log.d("MainFragment", "걸음수: $steps, 칼로리: $calories")

                val request = com.gbsb.routiemobile.dto.HealthDataDto(steps, calories)

                // 서버 전송 (userId 없이)
                com.gbsb.routiemobile.network.RetrofitClient.healthApi
                    .sendHealthData(request)
                    .enqueue(object : retrofit2.Callback<Void> {
                        override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                            if (response.isSuccessful) {
                                Log.d("MainFragment", "헬스 데이터 전송 성공")
                            } else {
                                Log.e("MainFragment", "헬스 데이터 전송 실패: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                            Log.e("MainFragment", "헬스 데이터 전송 오류: ${t.message}")
                        }
                    })
            },
            onError = { error ->
                Log.e("MainFragment", "Samsung Health 오류: $error")
            }
        )
    }

    private fun getStartOfWeek(date: LocalDate): LocalDate {
        return date.with(DayOfWeek.MONDAY)
    }

    private fun showWeekDayButtons(startOfWeek: LocalDate, selected: LocalDate) {
        val weekDays = (0..6).map { offset ->
            val date = startOfWeek.plusDays(offset.toLong())
            WeekDay(
                date = date,
                dayOfWeek = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.KOREAN),
                isSelected = date == selected
            )
        }

        weekDayAdapter.updateDays(weekDays, selected)
    }

    private fun fetchRoutineLogs(userId: String, date: String) {
        RetrofitClient.routineLogApi.getRoutineLogsByDate(userId, date)
            .enqueue(object : Callback<List<RoutineLog>> {
                override fun onResponse(
                    call: Call<List<RoutineLog>>,
                    response: Response<List<RoutineLog>>
                ) {
                    if (response.isSuccessful) {
                        val logs = response.body() ?: emptyList()

                        Log.d("RoutineLog", "받아온 로그 개수: ${logs.size}")
                        logs.forEachIndexed { index, log ->
                            Log.d("RoutineLog", "[$index] 총 ${log.exerciseLogs.size}개 운동 포함됨")
                        }

                        val logAdapter = RoutineLogAdapter(logs)
                        binding.recyclerRoutineLogs.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerRoutineLogs.adapter = logAdapter
                    } else {
                        Log.e("RoutineLog", "응답 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<RoutineLog>>, t: Throwable) {
                    Log.e("RoutineLog", "API 호출 실패: ${t.message}")
                }
            })
    }

    private fun getUserIdFromPrefs(): String? {
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getString("userId", null)

    }

    private fun loadScheduledRoutines(userId: String, date: String) {
        RetrofitClient.routineApi.getScheduledRoutines(userId, date)
            .enqueue(object : Callback<List<Routine>> {
                override fun onResponse(call: Call<List<Routine>>, response: Response<List<Routine>>) {
                    if (response.isSuccessful) {
                        val routines = response.body().orEmpty()

                        if (routines.isEmpty()) {
                            binding.recyclerRoutineList.adapter = null
                            binding.tvExerciseNames.text = "해당 날짜에 등록된 루틴이 없습니다."
                            return
                        }

                        binding.recyclerRoutineList.adapter = RoutineAdapter(
                            routines.toMutableList(),
                            onDeleteClick = {},
                            onItemClick = { routine ->
                                loadExercisesForRoutine(routine.id, routine.name)
                            }
                        )

                        // 첫 번째 루틴 자동 표시
                        val first = routines[0]
                        loadExercisesForRoutine(first.id, first.name)

                        setupRoutineDropdown(routines)

                    } else {
                        Toast.makeText(requireContext(), "예약된 루틴 불러오기 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Routine>>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadExercisesForRoutine(routineId: Long, routineName: String) {
        RetrofitClient.routineApi.getExercisesByRoutine(routineId.toString())
            .enqueue(object : Callback<List<ExerciseResponse>> {
                override fun onResponse(
                    call: Call<List<ExerciseResponse>>,
                    response: Response<List<ExerciseResponse>>
                ) {
                    if (response.isSuccessful) {
                        val exercises = response.body().orEmpty()
                        updateSketchbookText(exercises)
                    } else {
                        Toast.makeText(requireContext(), "운동 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<ExerciseResponse>>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateSketchbookText(exercises: List<ExerciseResponse>) {
        val exerciseText = exercises.joinToString("\t\t\t") { it.exerciseName }
        val displayText = if (exerciseText.isNotEmpty()) exerciseText else "운동이 없습니다."
        binding.tvExerciseNames.text = "\n\n$displayText"
    }

    private fun setupRoutineDropdown(routines: List<Routine>) {
        val routineNameButton = binding.dropdownRoutineName

        // 기본 표시 (첫 번째 루틴)
        if (routines.isNotEmpty()) {
            val first = routines[0]
            routineNameButton.text = first.name
            loadExercisesForRoutine(first.id, first.name)
        }

        routineNameButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), routineNameButton)
            routines.forEach { routine ->
                popupMenu.menu.add(routine.name)
            }

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val selectedRoutine = routines.find { it.name == menuItem.title }
                selectedRoutine?.let {
                    routineNameButton.text = it.name
                    loadExercisesForRoutine(it.id, it.name)
                }
                true
            }

            popupMenu.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}