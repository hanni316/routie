package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.dto.GachaResultDto
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class GachaDialogFragment : DialogFragment() {
    data class GachaItem(val itemId: Long, val name: String, val imageResId: Int)

    private var ticketCount = 0
    private lateinit var textTicketCount: TextView
    private val rareItem = GachaItem(127L, name = "모히칸천사", imageResId = R.drawable.angel)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.gacha_dialog, container, false)

        val btnDraw = view.findViewById<Button>(R.id.btn_do_gacha)
        val tvResult = view.findViewById<TextView>(R.id.text_gacha_result)
        val imgResult = view.findViewById<ImageView>(R.id.result_image)
        textTicketCount = view.findViewById(R.id.text_ticket_count2)


        val userId = requireContext()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("userId", null)

        if (userId != null) {
            loadTicketCount(userId)
        }

        btnDraw.setOnClickListener {
            if (ticketCount <= 0) {
                Toast.makeText(requireContext(), "티켓이 부족합니다!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId != null) {
                useTicket(userId) {
                    // 티켓 차감 후
                    ticketCount -= 1
                    textTicketCount.text = "보유 티켓: ${ticketCount}장"

                    // 가챠 시도
                    val result = tryGacha(0.01)
                    val isSuccess = result != null
                    val isHiddenItem = result?.itemId == 127L // 히든 조건 (네 기준으로 조정 가능)

                    // 결과 UI 반영
                    if (isSuccess) {
                        tvResult.text = "🎉${result!!.name} 당첨!"
                        imgResult.setImageResource(result.imageResId)
                    } else {
                        tvResult.text = "꽝! 다시 도전하세요!"
                        imgResult.setImageResource(R.drawable.fail_ball)
                    }

                    // 서버에 가챠 결과 전송
                    val dto = GachaResultDto(
                        userId = userId,
                        itemId = result?.itemId,
                        isSuccess = isSuccess,
                        isHiddenItem = isHiddenItem
                    )

                    RetrofitClient.userItemApi.sendGachaResult(dto)
                        .enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Log.d("Gacha", "서버 저장 성공")
                                } else {
                                    Log.w("Gacha", "응답 실패: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("Gacha", "서버 요청 실패", t)
                            }
                        })
                }
            }
        }
        return view
    }

    //티켓 수 로딩
    private fun loadTicketCount(userId: String) {
        RetrofitClient.ticketApi.getTicketCount(userId)
            .enqueue(object : retrofit2.Callback<com.gbsb.routiemobile.dto.TicketCountDto> {
                override fun onResponse(call: Call<com.gbsb.routiemobile.dto.TicketCountDto>, response: Response<com.gbsb.routiemobile.dto.TicketCountDto>) {
                    if (response.isSuccessful) {
                        ticketCount = response.body()?.ticketCount ?: 0
                        textTicketCount.text = "${ticketCount}장"
                    }
                }

                override fun onFailure(call: Call<com.gbsb.routiemobile.dto.TicketCountDto>, t: Throwable) {
                    Log.e("Gacha", "티켓 조회 실패", t)
                }
            })
    }

    //티켓 참감 함수
    private fun useTicket(userId: String, onSuccess: () -> Unit) {
        val dto = com.gbsb.routiemobile.dto.TicketUseRequestDto(userId, 1)
        RetrofitClient.ticketApi.useTicket(dto)
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        Toast.makeText(requireContext(), "티켓 사용 실패!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버 연결 실패!", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun tryGacha(probability: Double): GachaItem? {
        return if (Math.random() <= probability) rareItem else null
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog?.window?.setLayout(width, height)

    }
}