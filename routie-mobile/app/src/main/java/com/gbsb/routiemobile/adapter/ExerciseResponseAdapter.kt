package com.gbsb.routiemobile.adapter

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.dto.ExerciseResponse

class ExerciseResponseAdapter(
    private val exercises: MutableList<ExerciseResponse>,
    private val onDeleteClick: (ExerciseResponse) -> Unit
) : RecyclerView.Adapter<ExerciseResponseAdapter.VH>() {

    inner class VH(val container: LinearLayout) : RecyclerView.ViewHolder(container) {
        val tvName: TextView = container.findViewById(android.R.id.text1)
        val tvDelete: TextView = container.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val container = LinearLayout(parent.context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val nameTv = TextView(parent.context).apply {
            id = android.R.id.text1
            textSize = 21f
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }

        val deleteTv = TextView(parent.context).apply {
            id = android.R.id.text2
            text = "삭제   "
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        container.addView(nameTv)
        container.addView(deleteTv)
        return VH(container)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = exercises[position]
        holder.tvName.text = item.exerciseName
        holder.tvDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount(): Int = exercises.size

    fun submitList(newList: List<ExerciseResponse>) {
        exercises.apply {
            clear()
            addAll(newList)
        }
        notifyDataSetChanged()
    }
}
