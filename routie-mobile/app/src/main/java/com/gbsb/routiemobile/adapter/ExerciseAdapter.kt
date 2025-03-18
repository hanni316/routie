package com.gbsb.routiemobile.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.gbsb.routiemobile.dto.Exercise

class ExerciseAdapter(private val exercises: MutableList<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView as TextView  // 직접 생성한 TextView를 사용
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val textView = TextView(parent.context).apply {
            textSize = 18f
            setPadding(16, 8, 16, 8)
        }
        return ExerciseViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name  // 운동 이름 설정
    }

    override fun getItemCount(): Int = exercises.size

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
        notifyItemInserted(exercises.size - 1)  // UI 업데이트
    }
}
