package com.gbsb.routiemobile.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {
    private val prefName = "LoginPrefs"
    private val keyUserId = "USER_ID"
    private val keyIsLoggedIn = "IS_LOGGED_IN"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

// 로그인 정보 저장 (userId만 저장)
    fun saveLoginInfo(userId: String) {
        editor.putString(keyUserId, userId)
        editor.putBoolean(keyIsLoggedIn, true)
    editor.apply()
    }


    // 저장된 사용자 ID 가져오기
    fun getUserId(): String? {
        return sharedPreferences.getString(keyUserId, null)
    }


    // 로그인 여부 확인
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(keyIsLoggedIn, false)
    }


    // ✅ 로그아웃 (저장된 데이터 삭제)
    fun logout() {
        editor.clear()
        editor.apply()
    }

}
