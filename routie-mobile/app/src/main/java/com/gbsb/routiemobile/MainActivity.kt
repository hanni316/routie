package com.gbsb.routiemobile

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.api.RewardApiService
import com.gbsb.routiemobile.dto.RewardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var btnFetchReward: Button
    private lateinit var textViewGold: TextView
    private lateinit var textViewSteps: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰 연결
        btnFetchReward = findViewById(R.id.btnFetchReward)
        textViewGold = findViewById(R.id.textViewGold)
        textViewSteps = findViewById(R.id.textViewSteps)

        // 버튼 클릭 시 API 요청
        btnFetchReward.setOnClickListener {
            getUserReward(1) // userId 1번 사용 (테스트용)
        }
    }

    private fun getUserReward(userId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.45.132:8080/") // 본인 서버 IP 맞는지 확인
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RewardApiService::class.java)

        apiService.getUserReward(userId).enqueue(object : Callback<RewardResponse> {
            override fun onResponse(
                call: Call<RewardResponse>,
                response: Response<RewardResponse>
            ) {
                if (response.isSuccessful) {
                    val reward = response.body()
                    runOnUiThread {
                        textViewGold.text = "Gold: ${reward?.totalGold ?: 0}"
                        textViewSteps.text = "Steps: ${reward?.totalSteps ?: 0}"
                    }
                    Log.d(
                        "API_SUCCESS",
                        "User ID: $userId, Gold: ${reward?.totalGold}, Steps: ${reward?.totalSteps}"
                    )
                } else {
                    Log.e("API_FAILURE", "Error: ${response.errorBody()?.string()}")
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "API 요청 실패: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<RewardResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failed to fetch reward", t)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}