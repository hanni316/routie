package com.gbsb.routiemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.dto.ExerciseResponse
import com.google.android.material.card.MaterialCardView
import android.widget.TextView
import android.view.Gravity

import android.widget.FrameLayout

import androidx.core.content.ContextCompat


class ExerciseResponseAdapter(
    private val exercises: MutableList<ExerciseResponse>,
    private val onDeleteClick: (ExerciseResponse) -> Unit
) : RecyclerView.Adapter<ExerciseResponseAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioSelect: AppCompatRadioButton = itemView.findViewById(R.id.radioSelect)
        val imgIcon: AppCompatImageView       = itemView.findViewById(R.id.imgIcon)
        val tvName: TextView                  = itemView.findViewById(R.id.tvExerciseName)

        private val tvDelete: TextView = TextView(itemView.context).apply {
            text = "삭제"
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            setPadding(16, 8, 16, 8)
        }
        init {
            (itemView as ViewGroup).addView(
                tvDelete,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    marginEnd = 16
                }
            )
            tvDelete.setOnClickListener {
                val pos = getAdapterPosition()
                if (pos != RecyclerView.NO_POSITION) {
                    onDeleteClick(exercises[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ex = exercises[position]
        holder.tvName.text = ex.exerciseName
        holder.radioSelect.visibility = View.GONE

        // 아이콘 매핑 (name → drawable)
        val ctx = holder.itemView.context
        val resName = ex.enName
            .lowercase()
            .replace("\\s+".toRegex(), "_")
        val resId = ctx.resources.getIdentifier(
            resName, "drawable", ctx.packageName
        ).takeIf { it != 0 } ?: R.drawable.burpee
        holder.imgIcon.setImageResource(resId)
    }

    /** 외부에서 리스트 전체 교체할 때 사용 */
    fun submitList(newList: List<ExerciseResponse>) {
        exercises.clear()
        exercises.addAll(newList)
        notifyDataSetChanged()
    }
}
