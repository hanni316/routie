package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import com.gbsb.routiemobile.dto.Item
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

    private var ticketCount = 0
    private lateinit var textTicketCount: TextView
    private var gachaPool: List<Item> = emptyList()

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

        if (userId == null) {
            Toast.makeText(requireContext(), "유저 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            dismiss()  // 다이얼로그 닫기
            return view
        }

        loadTicketCount(userId)
        loadGachaPool()

        btnDraw.setOnClickListener {
            if (ticketCount <= 0) {
                Toast.makeText(requireContext(), "티켓이 부족합니다!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (gachaPool.isEmpty()) {
                Toast.makeText(requireContext(), "가챠 아이템을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnDraw.isEnabled = false

            if (userId != null) {
                useTicket(userId) {
                    // 티켓 차감 후
                    ticketCount -= 1
                    textTicketCount.text = "${ticketCount}장"

                    // 가챠 시도
                    val result = tryGachaFromPool(0.5) //테스트용 50%
                    val isSuccess = result != null
                    val isHiddenItem = result?.itemId

                    // 결과 UI 반영
                    if (isSuccess) {
                        tvResult.text = "${result!!.name} 당첨!"
                        imgResult.setImageResource(getDrawableResIdByName(result.nameEn))
                    } else {
                        tvResult.text = "꽝! 다시 도전하세요!"
                        imgResult.setImageResource(R.drawable.fail_ball)
                    }

                    // 서버에 가챠 결과 전송
                    val dto = GachaResultDto(
                        userId = userId,
                        itemId = result?.itemId?.toLong() ?: -1L,
                        isSuccess = isSuccess,
                        isHiddenItem = result?.gachaItem == true
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
    private fun loadGachaPool() {
        RetrofitClient.shopApiService.getGachaItems()
            .enqueue(object : retrofit2.Callback<List<Item>> {
                override fun onResponse(
                    call: Call<List<Item>>,
                    response: Response<List<Item>>
                ) {
                    if (response.isSuccessful) {
                        gachaPool = response.body() ?: emptyList()
                    }
                }
                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    Log.e("Gacha", "가챠 풀 로딩 실패", t)
                }
            })
    }

    // 가챠 추첨
    private fun tryGachaFromPool(probability: Double): Item? {
        if (Math.random() > probability) return null
        if (gachaPool.isEmpty()) return null
        val idx = (Math.random() * gachaPool.size).toInt()
        return gachaPool[idx]
    }

    private fun getDrawableResIdByName(nameEn: String): Int {
        val id = resources.getIdentifier(nameEn, "drawable", requireContext().packageName)
        return if (id != 0) id else R.drawable.fail_ball
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

    //티켓 차감 함수
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


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog?.window?.setLayout(width, height)

    }
}