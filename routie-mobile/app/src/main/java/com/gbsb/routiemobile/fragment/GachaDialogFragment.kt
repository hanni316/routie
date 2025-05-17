package com.gbsb.routiemobile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.gbsb.routiemobile.R

class GachaDialogFragment : DialogFragment() {
    data class GachaItem(val name: String, val imageResId: Int)

    private  val rareItem = GachaItem("모히칸 천사", R.drawable.angel)
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
                // 당첨 시 DB 저장 (추후 구현)
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