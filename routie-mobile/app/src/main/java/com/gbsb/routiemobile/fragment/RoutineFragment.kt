package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.RoutineAdapter
import com.gbsb.routiemobile.dto.Routine
import com.gbsb.routiemobile.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutineFragment : Fragment() {

    // 루틴 목록 데이터
    private val routineList = mutableListOf<Routine>()

    // RecyclerView 어댑터
    private lateinit var routineAdapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_routine, container, false)

        val createRoutineButton: MaterialButton = view.findViewById(R.id.createRoutineButton)
        createRoutineButton.setOnClickListener {
            parentFragmentManager.commit {
                findNavController().navigate(R.id.MakingroutineFragment)
            }
        }

        // RecyclerView 설정
        val recyclerRoutine: RecyclerView = view.findViewById(R.id.recyclerRoutine)
        routineAdapter = RoutineAdapter(
            routineList,
            onDeleteClick = { routine ->
                // 삭제 확인 다이얼로그
                showDeleteConfirmationDialog(routine)
            },
            onItemClick = { routine ->
                // 아이템 전체를 클릭하면 수정 화면으로 이동
                goToModifyRoutine(routine)
            }
        )
        recyclerRoutine.layoutManager = LinearLayoutManager(requireContext())
        recyclerRoutine.adapter = routineAdapter

        // 서버에서 루틴 목록 불러오기
        loadUserRoutines()

        return view
    }

    private fun loadUserRoutines() {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", 0)
        val userId = sharedPref.getString("userId", null)
        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.routineApi.getUserRoutines(userId).enqueue(object : Callback<List<Routine>> {
            override fun onResponse(call: Call<List<Routine>>, response: Response<List<Routine>>) {
                if (response.isSuccessful) {
                    val routines = response.body()
                    Log.d("ROUTINE_API", "서버 응답: $routines")

                    routineList.clear()
                    routines?.let {
                        routineList.addAll(it)
                    }
                    routineAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "루틴을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Routine>>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 아이템 클릭 시 루틴 수정 화면으로 이동
    private fun goToModifyRoutine(routine: Routine) {
        val bundle = bundleOf("routineId" to routine.id)
        findNavController().navigate(R.id.action_routineFragment_to_modifyRoutineFragment, bundle)

    }

    // 삭제 확인 다이얼로그
    private fun showDeleteConfirmationDialog(routine: Routine) {
        AlertDialog.Builder(requireContext())
            .setTitle("")
            .setMessage("루틴을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                removeRoutine(routine)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun removeRoutine(routine: Routine) {
        RetrofitClient.routineApi.deleteRoutine(routine.id.toString()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val index = routineList.indexOf(routine)
                    if (index != -1) {
                        routineList.removeAt(index)
                        routineAdapter.notifyItemRemoved(index)
                        Toast.makeText(requireContext(), "루틴이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "루틴 삭제 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
