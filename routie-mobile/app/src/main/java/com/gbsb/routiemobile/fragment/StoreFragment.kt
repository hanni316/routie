package com.gbsb.routiemobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class StoreFragment : Fragment() {
   // private var param1: String? = null
   // private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           // param1 = it.getString(ARG_PARAM1)
           // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val backIcon = view.findViewById<ImageView>(R.id.arrowButton)
        backIcon.setOnClickListener {
            findNavController().navigateUp() // 이전 Fragment(MainFragment)로 이동
        }
    }
}