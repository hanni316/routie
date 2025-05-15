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
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.RoutineAdapter
import com.gbsb.routiemobile.adapter.RoutineLogAdapter
import com.gbsb.routiemobile.adapter.WeekDayAdapter
import com.gbsb.routiemobile.databinding.FragmentMainBinding
import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import com.gbsb.routiemobile.dto.ExerciseResponse
import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.dto.RoutineDayResponse
import com.gbsb.routiemobile.dto.RoutineLog
import com.gbsb.routiemobile.dto.WeekDay
import com.gbsb.routiemobile.network.RetrofitClient
import kotlinx.coroutines.launch
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

    private lateinit var bodyImage: ImageView
    private lateinit var hairImage: ImageView
    private lateinit var outfitImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var shoesImage: ImageView
    private lateinit var accessoryImage: ImageView

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
            fetchRoutineLogsForDate(selectedDate)
            loadRoutinesByDayOfWeekAndDate(selectedDate)
        }

        binding.recyclerWeekDays.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = weekDayAdapter
        }

        binding.btnSelectdate.setOnClickListener {
            val dialog = DatePickerDialog(requireContext(), { _, y, m, d ->
                binding.txtNowdate.text = "$y 년 ${m + 1} 월"
                val cal = Calendar.getInstance()
                cal.set(y, m, d)
                selectedDate = LocalDate.of(y, m + 1, d)
                showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)

                fetchRoutineLogsForDate(selectedDate)
                loadRoutinesByDayOfWeekAndDate(selectedDate)

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
        fetchRoutineLogsForDate(selectedDate)
        loadRoutinesByDayOfWeekAndDate(selectedDate)

        bodyImage = binding.bodyImage
        hairImage = binding.hairImage
        outfitImage = binding.outfitImage
        bottomImage = binding.bottomImage
        accessoryImage = binding.accessoryImage
        shoesImage = binding.shoesImage

        val userId = getUserIdFromPrefs()
        userId?.let {
            fetchRoutineLogs(it, selectedDate.toString())
        }

        getUserIdFromPrefs()?.let { loadCharacterStyle(it) }
        showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)


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

        binding.imgMyroom.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_myroomFragment)
        }

        binding.imgRanking.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_rankingFragment)
        }

        // 캐릭터 스타일 불러오기
        getUserIdFromPrefs()?.let { userId ->
            loadCharacterStyle(userId)
        }

        // 초기 표시
        showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)

        // 앱 처음 진입 시 오늘 날짜 기준 루틴 로그 표시
        getUserIdFromPrefs()?.let {
            fetchRoutineLogs(it, selectedDate.toString())
        }

        val samsungHealthManager =
            com.gbsb.routiemobile.health.SamsungHealthManager(requireContext())

        samsungHealthManager.fetchStepsAndCalories(
            onResult = { steps, calories ->
                Log.d("MainFragment", "걸음수: $steps, 칼로리: $calories")

                val request = com.gbsb.routiemobile.dto.HealthDataDto(steps, calories)

                // 서버 전송 (userId 없이)
                com.gbsb.routiemobile.network.RetrofitClient.healthApi
                    .sendHealthData(request)
                    .enqueue(object : retrofit2.Callback<Void> {
                        override fun onResponse(
                            call: retrofit2.Call<Void>,
                            response: retrofit2.Response<Void>
                        ) {
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
                dayOfWeek = date.dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    Locale.KOREAN
                ),
                isSelected = date == selected
            )
        }

        weekDayAdapter.updateDays(weekDays, selected)
    }

    private fun fetchRoutineLogsForDate(date: LocalDate) {
        getUserIdFromPrefs()?.let { userId ->
            fetchRoutineLogs(userId, date.toString())
        }
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

                        val totalCalories = logs.sumOf { it.totalCaloriesBurned }
                        val totalDuration = logs.sumOf { it.totalDuration }  // 초 단위

                        val hours = totalDuration / 3600
                        val minutes = (totalDuration % 3600) / 60
                        val seconds = totalDuration % 60

                        val timeText = buildString {
                            if (hours > 0) append("${hours}시간 ")
                            if (minutes > 0 || hours > 0) append("${minutes}분 ")
                            append("${seconds}초")
                        }

                        binding.tvDailyText.text =
                            "총 소모 칼로리: ${totalCalories}kcal\n총 운동 시간: $timeText"


                        val logAdapter = RoutineLogAdapter(logs)
                        binding.recyclerRoutineLogs.layoutManager =
                            LinearLayoutManager(requireContext())
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

    private fun loadRoutinesByDayOfWeekAndDate(targetDate: LocalDate) {
        val dayOfWeek = when (targetDate.dayOfWeek) {
            DayOfWeek.MONDAY -> "monday"
            DayOfWeek.TUESDAY -> "tuesday"
            DayOfWeek.WEDNESDAY -> "wednesday"
            DayOfWeek.THURSDAY -> "thursday"
            DayOfWeek.FRIDAY -> "friday"
            DayOfWeek.SATURDAY -> "saturday"
            DayOfWeek.SUNDAY -> "sunday"
        }

        // suspend 함수이므로 lifecycleScope로 감싸야 함
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val allRoutines = RetrofitClient.routineApi.getRoutinesByDay(dayOfWeek)
                val filtered = allRoutines.filter {
                    val createdDate = LocalDate.parse(it.scheduledDate)
                    !targetDate.isBefore(createdDate)
                }

                if (filtered.isEmpty()) {
                    binding.recyclerRoutineList.adapter = null
                    binding.tvExerciseNames.text = "선택된 날짜에 해당하는 루틴이 없습니다."
                    binding.dropdownRoutineName.text = "루틴 없음"
                    setupRoutineDropdown(emptyList())

                    return@launch
                }

                val routines = filtered.map {
                    Routine(it.id, it.name, description = "", exercises = emptyList())
                }
                val adapter = RoutineAdapter(routines.toMutableList(), {}, {
                    loadExercisesForRoutine(it.id, it.name)
                })
                binding.recyclerRoutineList.adapter = adapter

                val first = routines[0]
                loadExercisesForRoutine(first.id, first.name)
                setupRoutineDropdown(routines)

            } catch (e: Exception) {
                Log.e("RoutineAPI", "루틴 로드 실패: ${e.message}")
            }
        }
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
                        Toast.makeText(requireContext(), "운동 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT)
                            .show()
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

    private fun getTodayDayOfWeekString(): String {
        return when (LocalDate.now().dayOfWeek) {
            DayOfWeek.MONDAY -> "monday"
            DayOfWeek.TUESDAY -> "tuesday"
            DayOfWeek.WEDNESDAY -> "wednesday"
            DayOfWeek.THURSDAY -> "thursday"
            DayOfWeek.FRIDAY -> "friday"
            DayOfWeek.SATURDAY -> "saturday"
            DayOfWeek.SUNDAY -> "sunday"
        }
    }

    private fun loadCharacterStyle(userId: String) {
        RetrofitClient.characterApi.getStyle(userId)
            .enqueue(object : Callback<CharacterStyleResponseDto> {
                override fun onResponse(
                    call: Call<CharacterStyleResponseDto>,
                    response: Response<CharacterStyleResponseDto>
                ) {
                    if (response.isSuccessful) {
                        val style = response.body()
                        style?.let {
                            updateCharacterImages(it)
                        }
                    } else {
                        Log.e("Character", "캐릭터 스타일 조회 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CharacterStyleResponseDto>, t: Throwable) {
                    Log.e("Character", "API 호출 실패: ${t.message}")
                }
            })
    }

    private fun updateCharacterImages(style: CharacterStyleResponseDto) {
        // 기본 몸통은 항상 표시

        // 헤어
        if (style.hair != null) {
            val hairResId = getDrawableResIdByName(style.hair)
            hairImage.setImageResource(hairResId)
        } else {
            hairImage.setImageDrawable(null)
        }

        // 상의
        if (style.outfit != null) {
            val outfitResId = getDrawableResIdByName(style.outfit)
            outfitImage.setImageResource(outfitResId)
        } else {
            outfitImage.setImageDrawable(null)
        }

        // 하의
        if (style.bottom != null) {
            val bottomResId = getDrawableResIdByName(style.bottom)
            bottomImage.setImageResource(bottomResId)
        } else {
            bottomImage.setImageDrawable(null)
        }

        // 악세서리
        if (style.accessory != null) {
            val accessoryResId = getDrawableResIdByName(style.accessory)
            accessoryImage.setImageResource(accessoryResId)
        } else {
            accessoryImage.setImageDrawable(null)
        }

        // 신발
        if (style.shoes != null) {
            val shoesResId = getDrawableResIdByName(style.shoes)
            shoesImage.setImageResource(shoesResId)
        } else {
            shoesImage.setImageDrawable(null)
        }
    }

    private fun getDrawableResIdByName(resourceName: String): Int {
        return resources.getIdentifier(resourceName, "drawable", requireContext().packageName)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}