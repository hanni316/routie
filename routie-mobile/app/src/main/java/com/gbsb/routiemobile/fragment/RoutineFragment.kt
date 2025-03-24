package com.gbsb.routiemobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R

class RoutineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_routine, container, false)

        // 버튼 클릭 시 MakingRoutineFragment로 이동
        val createRoutineButton: ImageButton = view.findViewById(R.id.createRoutineButton)
        createRoutineButton.setOnClickListener {
            parentFragmentManager.commit {
                findNavController().navigate(R.id.MakingroutineFragment)
            }
        }

        return view
    }
}
