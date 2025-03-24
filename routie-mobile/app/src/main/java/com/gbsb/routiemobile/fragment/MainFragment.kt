package com.gbsb.routiemobile.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.WeekDayAdapter
import com.gbsb.routiemobile.databinding.FragmentMainBinding
import com.gbsb.routiemobile.dto.WeekDay
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

class MainFragment : Fragment() {

    private var isNoticeBubbleVisible = false
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: LocalDate = LocalDate.now()

    // ✅ 어댑터를 전역 변수로 유지
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

        // ✅ 어댑터 초기화
        weekDayAdapter = WeekDayAdapter(emptyList()) { newlySelectedDate ->
            selectedDate = newlySelectedDate
            showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)
            Log.d("DATE_SELECT", "선택된 날짜: $selectedDate")
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

        // 초기 주간 버튼 표시
        showWeekDayButtons(getStartOfWeek(selectedDate), selectedDate)
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

        // 어댑터에 데이터 갱신
        weekDayAdapter.updateDays(weekDays, selected)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}