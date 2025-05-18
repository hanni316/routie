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

    private  val rareItem = GachaItem(3L, name = "모히칸천사", imageResId = R.drawable.angel)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.gacha_dialog, container, false)

        val btnDraw = view.findViewById<Button>(R.id.btn_do_gacha)
        val tvResult = view.findViewById<TextView>(R.id.text_gacha_result)
        val imgResult = view.findViewById<ImageView>(R.id.result_image)

        btnDraw.setOnClickListener {
            val result = tryGacha(0.02) // 2% 확률..^^ 극악 일수록 좋잖아요...
            if (result != null) {
                tvResult.text = "🎉${result.name} 당첨!"
                imgResult.setImageResource(result.imageResId)

                //SharedPreferences에서 userId 꺼내기
                val userId = requireContext()
                    .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    .getString("userId", null)
                if (userId != null) {
                    val dto = GachaResultDto(
                        userId = userId,
                        itemId = result.itemId
                    )

                    RetrofitClient.userItemApi.sendGachaResult(dto)
                        .enqueue(object : retrofit2.Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Log.d("Gacha", "서버 저장 성공")
                                } else {
                                    Log.w("Gacha", "서버 응답 실패: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("Gacha", "서버 요청 실패", t)
                            }
                        })
                } else {
                    Toast.makeText(requireContext(), "로그인 정보 없음", Toast.LENGTH_SHORT).show()
                }

            } else {
                tvResult.text = "꽝! 다시 도전하세요!"
                imgResult.setImageResource(R.drawable.fail_ball)
            }
        }

        return view
    }

    private fun tryGacha(probability: Double): GachaItem? {
        return if (Math.random() <= probability) rareItem else null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}