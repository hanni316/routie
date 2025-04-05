package com.gbsb.routiemobile.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.ItemRoutineLogBinding
import com.gbsb.routiemobile.dto.RoutineLog

class RoutineLogAdapter(private val logs: List<RoutineLog>) :
    RecyclerView.Adapter<RoutineLogAdapter.RoutineLogViewHolder>() {

    inner class RoutineLogViewHolder(val binding: ItemRoutineLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineLogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRoutineLogBinding.inflate(inflater, parent, false)
        return RoutineLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutineLogViewHolder, position: Int) {
        val context = holder.itemView.context
        val customFont = ResourcesCompat.getFont(context, R.font.ownglyp)

        val log = logs[position]
        val layout = holder.binding.layoutExerciseList

        // 기존 뷰 초기화
        layout.removeAllViews()

        log.exerciseLogs.forEach { exercise ->
            // 수평 LinearLayout 생성
            val rowLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // 운동명
            val nameView = TextView(context).apply {
                text = exercise.exerciseName
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                typeface = customFont
                setTextColor(Color.BLACK)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }

            // 운동 시간
            val timeView = TextView(context).apply {
                text = formatTime(exercise.duration)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                typeface = customFont
                setTextColor(Color.BLACK)
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }

            // 칼로리
            val calView = TextView(context).apply {
                text = "${exercise.caloriesBurned} kcal"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                typeface = customFont
                setTextColor(Color.BLACK)
                gravity = Gravity.END
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }

            // 수평 row에 추가
            rowLayout.addView(nameView)
            rowLayout.addView(timeView)
            rowLayout.addView(calView)

            // 리스트에 추가
            layout.addView(rowLayout)
        }
    }

    override fun getItemCount(): Int = logs.size

    private fun formatTime(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }
}

