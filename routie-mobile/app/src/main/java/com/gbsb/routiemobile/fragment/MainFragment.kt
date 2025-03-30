package com.gbsb.routiemobile.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.RoutineLogAdapter
import com.gbsb.routiemobile.adapter.WeekDayAdapter
import com.gbsb.routiemobile.databinding.FragmentMainBinding
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
