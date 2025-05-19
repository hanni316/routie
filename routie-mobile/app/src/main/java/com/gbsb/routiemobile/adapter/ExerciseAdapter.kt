package com.gbsb.routiemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.dto.Exercise

class ExerciseAdapter(
    private val exercises: MutableList<Exercise>,
    private val onSelect: (Int) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // 현재 선택된 포지션 저장
    private var selectedPos = -1

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioSelect: AppCompatRadioButton = itemView.findViewById(R.id.radioSelect)
        val imgIcon: AppCompatImageView     = itemView.findViewById(R.id.imgIcon)
        val tvName: TextView                = itemView.findViewById(R.id.tvExerciseName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val ex = exercises[position]
        holder.tvName.text = ex.name

        // 아이콘 로딩 (name → drawable 리소스 매핑)
        val ctx = holder.itemView.context
        val resName = ex.enName
            .lowercase()
            .replace("\\s+".toRegex(), "_")
        val resId = ctx.resources.getIdentifier(
            resName, "drawable", ctx.packageName
        ).takeIf { it != 0 } ?: R.drawable.burpee
        holder.imgIcon.setImageResource(resId)

        holder.radioSelect.isChecked = (position == selectedPos)

        holder.itemView.setOnClickListener {
            val adapterPos = holder.adapterPosition
            if (adapterPos == RecyclerView.NO_POSITION) return@setOnClickListener

            // 이전/현재 선택 갱신
            val prev = selectedPos
            selectedPos = adapterPos
            notifyItemChanged(prev)
            notifyItemChanged(selectedPos)
            onSelect(selectedPos)
        }
        holder.radioSelect.setOnClickListener {
            holder.itemView.performClick()
        }
    }

    fun getSelectedExercise(): Exercise? {
        return exercises.getOrNull(selectedPos)
    }
    fun submitList(newList: List<Exercise>) {
        exercises.clear()
        exercises.addAll(newList)
        selectedPos = -1       // 이전 선택 초기화
        notifyDataSetChanged()
    }
}
