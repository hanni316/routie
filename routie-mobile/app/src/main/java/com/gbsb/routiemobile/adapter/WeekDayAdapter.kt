package com.gbsb.routiemobile.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.ItemWeekDayBinding
import com.gbsb.routiemobile.dto.WeekDay
import java.time.LocalDate

class WeekDayAdapter(
    private var days: List<WeekDay>,
    private val onDayClick: (LocalDate) -> Unit
) : RecyclerView.Adapter<WeekDayAdapter.DayViewHolder>() {

    private var selectedPosition = -1

    inner class DayViewHolder(val binding: ItemWeekDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: WeekDay, isSelected: Boolean) {
            binding.tvDay.text = day.dayOfWeek
            binding.tvDate.text = day.date.dayOfMonth.toString()

            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.selected_date_bg)
                binding.tvDay.setTextColor(Color.WHITE)
                binding.tvDate.setTextColor(Color.WHITE)
            } else {
                binding.root.background = null
                binding.tvDay.setTextColor(Color.BLACK)
                binding.tvDate.setTextColor(Color.BLACK)
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onDayClick(day.date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeekDayBinding.inflate(inflater, parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.bind(day, position == selectedPosition)
        Log.d("WeekDayAdapter", "selectedPosition = $selectedPosition, position = $position, isSelected = ${position == selectedPosition}")
    }

    override fun getItemCount(): Int = days.size

    fun updateDays(newDays: List<WeekDay>, selectedDate: LocalDate) {
        this.days = newDays
        selectedPosition = newDays.indexOfFirst { it.date == selectedDate }
        notifyDataSetChanged()
    }
}
