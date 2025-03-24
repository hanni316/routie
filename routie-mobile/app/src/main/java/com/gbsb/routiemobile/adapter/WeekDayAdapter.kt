package com.gbsb.routiemobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.databinding.ItemWeekDayBinding
import com.gbsb.routiemobile.dto.WeekDay

class WeekDayAdapter(
    private val days: List<WeekDay>,
    private val onDayClick: (LocalDate: java.time.LocalDate) -> Unit
) : RecyclerView.Adapter<WeekDayAdapter.DayViewHolder>() {

    inner class DayViewHolder(val binding: ItemWeekDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeekDayBinding.inflate(inflater, parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.binding.tvDay.text = day.dayOfWeek
        holder.binding.tvDate.text = day.date.dayOfMonth.toString()

        holder.binding.root.isSelected = day.isSelected
        holder.binding.root.setOnClickListener {
            onDayClick(day.date)
        }
    }

    override fun getItemCount(): Int = days.size
}
