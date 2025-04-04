package com.gbsb.routiemobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.content.Context
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R



class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        view.findViewById<ImageButton>(R.id.accountBtn).setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_accountFragment)
        }

        view.findViewById<ImageButton>(R.id.alarmSetBtn).setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_alarmFragment)
        }

        view.findViewById<ImageButton>(R.id.userInfoModBtn).setOnClickListener {
            navController.navigate(R.id.action_settingFragment_to_userInfoFragment)
        }

        view.findViewById<ImageButton>(R.id.logoutBtn).setOnClickListener {
            logoutUser()
            // 로그아웃 처리 후 LoginFragment로 이동
            navController.navigate(R.id.action_settingFragment_to_loginFragment)
        }
    }
            private fun logoutUser() {
                val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    remove("isLoggedIn") // 자동 로그인 정보 삭제
                    remove("userId") // 저장된 사용자 ID 삭제
                    apply()
                }
            }
}