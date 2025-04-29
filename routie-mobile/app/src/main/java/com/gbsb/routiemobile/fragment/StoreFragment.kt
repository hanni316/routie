// 파일: app/src/main/kotlin/com/gbsb/routiemobile/fragment/StoreFragment.kt
package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.gbsb.routiemobile.adapter.StoreAdapter
import com.gbsb.routiemobile.databinding.FragmentStoreBinding
import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import com.gbsb.routiemobile.dto.Item
import com.gbsb.routiemobile.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    // RecyclerView Adapter
    private lateinit var adapter: StoreAdapter

    // 캐릭터 스타일 ImageView
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
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bodyImage = binding.bodyImage
        hairImage = binding.hairImage
        outfitImage = binding.outfitImage
        bottomImage = binding.bottomImage
        shoesImage = binding.shoesImage
        accessoryImage = binding.accessoryImage

        adapter = StoreAdapter { item ->
            val resId = getDrawableResIdByName(item.nameEn)
            when (item.categoryName) {
                "상의" -> outfitImage.setImageResource(resId)    // 상의
                "하의" -> bottomImage.setImageResource(resId)    // 하의
                "신발" -> shoesImage.setImageResource(resId)     // 신발
                "악세서리" -> accessoryImage.setImageResource(resId) // 악세서리
                "헤어" -> hairImage.setImageResource(resId)      // 헤어
            }
        }

        // RecyclerView 그리드 세팅
        binding.itemBox.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            adapter = this@StoreFragment.adapter
        }

        // 카테고리 버튼 클릭 리스너
        listOf(
            binding.btnTop       to 1,
            binding.btnBottom    to 2,
            binding.btnShoes     to 3,
            binding.btnAccessory to 4,
            binding.btnHair      to 5
        ).forEach { (btn, categoryId) ->
            btn.setOnClickListener { clickedView ->
                highlightButton(clickedView)
                loadItems(categoryId)
            }
        }

        // 초기 선택: 상의 카테고리
        binding.btnTop.performClick()

        // 캐릭터 스타일 로드
        getUserIdFromPrefs()?.let { loadCharacterStyle(it) }
    }

    // 카테고리 아이템 불러오기
    private fun loadItems(categoryId: Int) {
        lifecycleScope.launch {
            try {
                val items: List<Item> =
                    RetrofitClient.shopApiService.getItemsByCategory(categoryId)
                adapter.submitList(items)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "아이템 불러오기 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // 선택된 버튼만 강조
    private fun highlightButton(selected: View) {
        listOf(
            binding.btnTop,
            binding.btnBottom,
            binding.btnShoes,
            binding.btnAccessory,
            binding.btnHair
        ).forEach { btn ->
            btn.alpha = if (btn == selected) 1f else 0.5f
        }
    }

    private fun getUserIdFromPrefs(): String? {
        val prefs = requireContext()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return prefs.getString("userId", null)
    }

    // 캐릭터 스타일 API 호출
    private fun loadCharacterStyle(userId: String) {
        RetrofitClient.characterApi.getStyle(userId)
            .enqueue(object : Callback<CharacterStyleResponseDto> {
                override fun onResponse(
                    call: Call<CharacterStyleResponseDto>,
                    response: Response<CharacterStyleResponseDto>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { updateCharacterImages(it) }
                    }
                }
                override fun onFailure(call: Call<CharacterStyleResponseDto>, t: Throwable) {
                    Log.e("StoreFragment", "캐릭터 스타일 불러오기 실패: ${t.message}")
                }
            })
    }

    // 캐릭터 이미지 업데이트
    private fun updateCharacterImages(style: CharacterStyleResponseDto) {
        style.hair?.let {
            hairImage.setImageResource(getDrawableResIdByName(it))
        } ?: hairImage.setImageDrawable(null)

        style.outfit?.let {
            outfitImage.setImageResource(getDrawableResIdByName(it))
        } ?: outfitImage.setImageDrawable(null)

        style.bottom?.let {
            bottomImage.setImageResource(getDrawableResIdByName(it))
        } ?: bottomImage.setImageDrawable(null)

        style.accessory?.let {
            accessoryImage.setImageResource(getDrawableResIdByName(it))
        } ?: accessoryImage.setImageDrawable(null)

        style.shoes?.let {
            shoesImage.setImageResource(getDrawableResIdByName(it))
        } ?: shoesImage.setImageDrawable(null)
    }

    // drawable 조회
    private fun getDrawableResIdByName(resourceName: String): Int {
        return resources.getIdentifier(
            resourceName,
            "drawable",
            requireContext().packageName
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
