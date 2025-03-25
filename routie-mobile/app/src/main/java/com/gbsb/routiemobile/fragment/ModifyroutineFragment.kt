package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.dto.RoutineResponse
import com.gbsb.routiemobile.dto.RoutineUpdateRequest
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyroutineFragment : Fragment() {

    private var routineId: Long = 0L

    private lateinit var routineNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        routineId = arguments?.getLong("routineId") ?: 0L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modifyroutine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 요소 초기화
        routineNameEditText = view.findViewById(R.id.routineNameEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        saveButton = view.findViewById(R.id.save_button)

        // 서버에서 루틴 상세 정보를 불러와 EditText에 세팅
        loadRoutineDetail()

        // 저장 버튼 클릭 시 수정 API 호출
        saveButton.setOnClickListener {
            updateRoutine()
        }
    }

    // 루틴 상세 정보(루틴 이름, 루틴 설명)
    private fun loadRoutineDetail() {
        RetrofitClient.routineApi.getRoutineDetail(routineId.toString())
            .enqueue(object : Callback<RoutineResponse> {
                override fun onResponse(call: Call<RoutineResponse>, response: Response<RoutineResponse>) {
                    if (response.isSuccessful) {
                        val routine = response.body()
                        if (routine != null) {
                            routineNameEditText.setText(routine.name)
                            descriptionEditText.setText(routine.description)
                        }
                    } else {
                        Toast.makeText(requireContext(), "루틴 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RoutineResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
    }
    //루틴 이름, 루틴 설명 수정
    private fun updateRoutine() {
        val newName = routineNameEditText.text.toString().trim()
        val newDescription = descriptionEditText.text.toString().trim()


        if (newName.isEmpty()) {
            Toast.makeText(requireContext(), "루틴 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val routineUpdateRequest = RoutineUpdateRequest(
            name = newName,
            description = newDescription
        )
        //운동 추가, 삭제 추가예정

        RetrofitClient.routineApi.updateRoutine(routineId.toString(), routineUpdateRequest)
            .enqueue(object : Callback<RoutineResponse> {
                override fun onResponse(call: Call<RoutineResponse>, response: Response<RoutineResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "루틴이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        // 수정 성공 시 이전 화면으로 돌아갑니다.
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "루틴 수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RoutineResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
