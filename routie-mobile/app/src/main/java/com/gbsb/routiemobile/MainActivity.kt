package com.gbsb.routiemobile

import android.widget.*
import retrofit2.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.api.RewardApiService
import com.gbsb.routiemobile.dto.CaloriesRequest
import com.gbsb.routiemobile.dto.RewardResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var btnFetchReward: Button
    private lateinit var btnSendCalories: Button
    private lateinit var textViewGold: TextView
    private lateinit var textViewSteps: TextView
    private lateinit var editTextCalories: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰 연결
        btnFetchReward = findViewById(R.id.btnFetchReward)
        btnSendCalories = findViewById(R.id.btnSendCalories)
        textViewGold = findViewById(R.id.textViewGold)
        textViewSteps = findViewById(R.id.textViewSteps)
        editTextCalories = findViewById(R.id.editTextCalories)

        val userId = 1 // 테스트 용

        // 리워드 조회 버튼 클릭 시
        btnFetchReward.setOnClickListener {
            getUserReward(userId)
        }
        // 칼로리 전송 버튼 클릭 시
        btnSendCalories.setOnClickListener {
            val calories = editTextCalories.text.toString().toIntOrNull()
            if (calories != null) {
                sendCalories(userId, calories)
            } else {
                Toast.makeText(this, "Enter valid calorie value", Toast.LENGTH_SHORT).show()
            }
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

    private fun sendCalories(userId: Int, calories: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.45.132:8080/") // 서버 IP 확인
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RewardApiService::class.java)

        val request = CaloriesRequest(calories)

        apiService.sendCalories(userId, request).enqueue(object : Callback<RewardResponse> {
            override fun onResponse(
                call: Call<RewardResponse>,
                response: Response<RewardResponse>
            ) {
                if (response.isSuccessful) {
                    val updatedReward = response.body()
                    runOnUiThread {
                        textViewGold.text = "Gold: ${updatedReward?.totalGold ?: 0}"
                        textViewSteps.text = "Steps: ${updatedReward?.totalSteps ?: 0}"
                        Toast.makeText(this@MainActivity, "Calories sent!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Log.d(
                        "API_SUCCESS",
                        "Updated Gold: ${updatedReward?.totalGold}, Steps: ${updatedReward?.totalSteps}"
                    )
                } else {
                    Log.e("API_FAILURE", "Error: ${response.errorBody()?.string()}")
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to send calories",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<RewardResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failed to send calories", t)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}