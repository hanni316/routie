package com.gbsb.routiemobile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private var isNoticeBubbleVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ✅ 버튼 미리 찾기
        val buttonProfile = findViewById<ImageButton>(R.id.btn_profile)
        val buttonGoToTest = findViewById<Button>(R.id.buttonGoToTest)
        val textView: TextView = findViewById(R.id.txt_nowdate)
        val btnSelectDate: ImageButton = findViewById(R.id.btn_selectdate)
        val buttontest : Button = findViewById(R.id.buttontest)
        val btn_bell : ImageButton = findViewById(R.id.btn_bell)
        val bubble2 : ImageView = findViewById(R.id.img_noticefield)

        btn_bell.setOnClickListener {
            if (!isNoticeBubbleVisible) {
                bubble2.visibility = ImageView.VISIBLE
                isNoticeBubbleVisible = true
            } else {
                bubble2.visibility = ImageView.GONE
                isNoticeBubbleVisible = false
            }
        }

        // ✅ 프로필 버튼 클릭 시 SettingActivity 이동
        buttonProfile.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java) // 클래스명 수정
            startActivity(intent)
        }

        buttontest.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // ✅ 버튼 클릭 시 TestActivity로 이동
//        //buttonGoToTest.setOnClickListener {
//            val intent = Intent(this, TestActivity::class.java)
//            startActivity(intent)
//        }

        // ✅ 현재 날짜 가져오기
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // 1~12월 표시

        // ✅ 초기 값으로 현재 연/월 설정
        textView.text = "$year 년 $month 월"

        // ✅ 버튼 클릭 시 DatePickerDialog 표시
        btnSelectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, _ ->
                    textView.text = "$selectedYear 년 ${selectedMonth + 1} 월"
                },
                year,
                month - 1, // DatePicker는 0부터 시작하므로 -1 필요
                1
            )

            // 📌 일(day) 숨기기 (오류 방지)
            val dayPicker = datePickerDialog.datePicker.findViewById<android.view.View>(
                resources.getIdentifier("day", "id", "android")
            )
            dayPicker?.visibility = android.view.View.GONE // null 체크 추가

            datePickerDialog.show()
        }

        // ✅ 시스템 바 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }
}
