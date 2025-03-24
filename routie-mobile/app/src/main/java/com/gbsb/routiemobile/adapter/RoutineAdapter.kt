package com.gbsb.routiemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.dto.Routine

class RoutineAdapter(
    private val routineList: MutableList<Routine>,
    private val onDeleteClick: (Routine) -> Unit
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoutineName: TextView = itemView.findViewById(R.id.tvRoutineName)
        val btnDelete: TextView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        // 여기서 routine_item.xml을 inflate하여 ViewHolder를 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routine_item, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routineList[position]

        // routine_item.xml에 있는 TextView/버튼에 데이터 세팅
        holder.tvRoutineName.text = routine.name
        holder.btnDelete.setOnClickListener {
            // 삭제 로직 (서버 호출/목록에서 제거 등) 콜백으로 넘김
            onDeleteClick(routine)
        }
    }

    override fun getItemCount(): Int = routineList.size
}
