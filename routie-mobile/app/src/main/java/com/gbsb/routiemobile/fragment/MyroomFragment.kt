package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.databinding.FragmentMainBinding
import com.gbsb.routiemobile.databinding.FragmentMyroomBinding
import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyroomFragment : Fragment() {

    private var _binding: FragmentMyroomBinding? = null
    private val binding get() = _binding!!

    private lateinit var bodyImage: ImageView
    private lateinit var hairImage: ImageView
    private lateinit var outfitImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var shoesImage: ImageView
    private lateinit var accessoryImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bodyImage = binding.bodyImage
        hairImage = binding.hairImage
        outfitImage = binding.outfitImage
        bottomImage = binding.bottomImage
        accessoryImage = binding.accessoryImage
        shoesImage = binding.shoesImage

        // 캐릭터 스타일 불러오기
        getUserIdFromPrefs()?.let { userId ->
            loadCharacterStyle(userId)
        }

        // 상점 버튼 → StoreFragment로 이동
        view.findViewById<ImageButton>(R.id.btn_shop).setOnClickListener {
            findNavController().navigate(R.id.action_myroomFragment_to_storeFragment)
        }

        // 메인 버튼 → MainFragment로 이동
        view.findViewById<ImageButton>(R.id.btn_out).setOnClickListener {
            findNavController().navigate(R.id.action_myroomFragment_to_MainFragment)
        }
    }

    private fun getUserIdFromPrefs(): String? {
        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getString("userId", null)

    }

    private fun loadCharacterStyle(userId: String) {
        RetrofitClient.characterApi.getStyle(userId)
            .enqueue(object : Callback<CharacterStyleResponseDto> {
                override fun onResponse(
                    call: Call<CharacterStyleResponseDto>,
                    response: Response<CharacterStyleResponseDto>
                ) {
                    if (response.isSuccessful) {
                        val style = response.body()
                        style?.let {
                            updateCharacterImages(it)
                        }
                    } else {
                        Log.e("Character", "캐릭터 스타일 조회 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CharacterStyleResponseDto>, t: Throwable) {
                    Log.e("Character", "API 호출 실패: ${t.message}")
                }
            })
    }

    private fun updateCharacterImages(style: CharacterStyleResponseDto) {
        // 기본 몸통은 항상 표시

        // 헤어
        if (style.hair != null) {
            val hairResId = getDrawableResIdByName(style.hair)
            hairImage.setImageResource(hairResId)
        } else {
            hairImage.setImageDrawable(null)
        }

        // 상의
        if (style.outfit != null) {
            val outfitResId = getDrawableResIdByName(style.outfit)
            outfitImage.setImageResource(outfitResId)
        } else {
            outfitImage.setImageDrawable(null)
        }

        // 하의
        if (style.bottom != null) {
            val bottomResId = getDrawableResIdByName(style.bottom)
            bottomImage.setImageResource(bottomResId)
        } else {
            bottomImage.setImageDrawable(null)
        }

        // 악세서리
        if (style.accessory != null) {
            val accessoryResId = getDrawableResIdByName(style.accessory)
            accessoryImage.setImageResource(accessoryResId)
        } else {
            accessoryImage.setImageDrawable(null)
        }

        // 신발
        if (style.shoes != null) {
            val shoesResId = getDrawableResIdByName(style.shoes)
            shoesImage.setImageResource(shoesResId)
        } else {
            shoesImage.setImageDrawable(null)
        }
    }

    private fun getDrawableResIdByName(resourceName: String): Int {
        return resources.getIdentifier(resourceName, "drawable", requireContext().packageName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}