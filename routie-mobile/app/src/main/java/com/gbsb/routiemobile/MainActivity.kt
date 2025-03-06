package com.gbsb.routiemobile

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Test 화면으로 이동
        val button = findViewById<Button>(R.id.buttonGoToTest) // 버튼 ID 확인
        button.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)

            // 현재 연/월을 표시할 TextView
            val textView: TextView = findViewById(R.id.textView)
            val btnSelectDate: ImageButton = findViewById(R.id.btnSelectDate)

            // 현재 날짜 가져오기
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // 1~12월 표시

            // 초기 값으로 현재 연/월 설정
            textView.text = "$year 년 $month 월"

            // 버튼 클릭 시 DatePickerDialog 표시
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

                // 📌 일(day) 숨기기
                datePickerDialog.datePicker.findViewById<android.view.View>(
                    resources.getIdentifier("day", "id", "android")
                )?.visibility = android.view.View.GONE

                datePickerDialog.show()
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}