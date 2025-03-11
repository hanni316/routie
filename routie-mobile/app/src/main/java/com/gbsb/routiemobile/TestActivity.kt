package com.gbsb.routiemobile

import android.widget.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gbsb.routiemobile.api.RewardApiService
import com.gbsb.routiemobile.dto.CaloriesRequest
import com.gbsb.routiemobile.api.RewardResponse
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestActivity : AppCompatActivity() {

    private lateinit var btnFetchReward: Button
    private lateinit var btnSendCalories: Button
    private lateinit var editTextUserId: EditText
    private lateinit var textViewGold: TextView
    private lateinit var textViewSteps: TextView
    private lateinit var editTextCalories: EditText
    private lateinit var apiService: RewardApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // 뷰 연결
        btnFetchReward = findViewById(R.id.btnFetchReward)
        btnSendCalories = findViewById(R.id.btnSendCalories)
        editTextUserId = findViewById(R.id.editTextUserId)
        textViewGold = findViewById(R.id.textViewGold)
        textViewSteps = findViewById(R.id.textViewSteps)
        editTextCalories = findViewById(R.id.editTextCalories)

        apiService = RetrofitClient.api // RetrofitClient에서 API 인스턴스 가져오기

        // 리워드 조회 버튼 클릭 시
        btnFetchReward.setOnClickListener {
            val userId = editTextUserId.text.toString().trim()
            if (userId.isEmpty()) {
                Toast.makeText(this, "User ID를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            getUserReward(userId)
        }

        // 칼로리 전송 버튼 클릭 시
        btnSendCalories.setOnClickListener {
            val userId = editTextUserId.text.toString().trim()
            val caloriesStr = editTextCalories.text.toString().trim()

            if (userId.isEmpty() || caloriesStr.isEmpty()) {
                Toast.makeText(this, "User ID와 칼로리를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calories = caloriesStr.toInt()
            sendCalories(userId, calories)
        }
    }

    // 서버에서 리워드 조회
    private fun getUserReward(userId: String) {
        apiService.getUserReward(userId).enqueue(object : Callback<RewardResponse> {
            override fun onResponse(
                call: Call<RewardResponse>,
                response: Response<RewardResponse>
            ) {
                if (response.isSuccessful) {
                    val reward = response.body()
                    textViewGold.text = "Gold: ${reward?.totalGold ?: 0}"
                    textViewSteps.text = "Steps: ${reward?.totalSteps ?: 0}"

                    Log.d(
                        "API_SUCCESS",
                        "User ID: $userId, Gold: ${reward?.totalGold}, Steps: ${reward?.totalSteps}"
                    )
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API_FAILURE", "Error: $errorMessage")
                    Toast.makeText(
                        this@TestActivity,
                        "API 요청 실패 (${response.code()}): $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RewardResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failed to fetch reward", t)
                Toast.makeText(this@TestActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 서버로 칼로리 전송
    private fun sendCalories(userId: String, calories: Int) {
        val request = CaloriesRequest(calories)

        apiService.sendCalories(userId, request).enqueue(object : Callback<RewardResponse> {
            override fun onResponse(
                call: Call<RewardResponse>,
                response: Response<RewardResponse>
            ) {
                if (response.isSuccessful) {
                    val updatedReward = response.body()
                    textViewGold.text = "Gold: ${updatedReward?.totalGold ?: 0}"
                    textViewSteps.text = "Steps: ${updatedReward?.totalSteps ?: 0}"
                    Toast.makeText(this@TestActivity, "Calories sent!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API_FAILURE", "Error: $errorMessage")
                    Toast.makeText(this@TestActivity, "Failed to send calories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RewardResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failed to send calories", t)
                Toast.makeText(this@TestActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
