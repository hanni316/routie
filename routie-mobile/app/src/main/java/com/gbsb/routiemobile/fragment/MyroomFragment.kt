package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R

class MyroomFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 상점 버튼 → StoreFragment로 이동
        view.findViewById<ImageButton>(R.id.btn_shop).setOnClickListener {
            findNavController().navigate(R.id.action_myroomFragment_to_storeFragment)
        }

        // 메인 버튼 → MainFragment로 이동
        view.findViewById<ImageButton>(R.id.btn_out).setOnClickListener {
            findNavController().navigate(R.id.action_myroomFragment_to_MainFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_myroom, container, false)
    }
}