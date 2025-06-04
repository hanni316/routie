package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.gbsb.routiemobile.adapter.StoreAdapter
import com.gbsb.routiemobile.databinding.FragmentStoreBinding
import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import com.gbsb.routiemobile.dto.Item
import com.gbsb.routiemobile.dto.UserItem
import com.gbsb.routiemobile.dto.PurchaseRequest
import com.gbsb.routiemobile.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StoreAdapter
    private var purchasedIds: Set<Int> = emptySet()
    private val previewedItems = mutableSetOf<Item>()
    private var currentStyle: CharacterStyleResponseDto? = null

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

        adapter = StoreAdapter(
            onPreview = { item ->
                previewedItems.add(item)
                val resId = getDrawableResIdByName(item.nameEn)
                when (item.categoryName) {
                    "상의"     -> outfitImage.setImageResource(resId)
                    "하의"     -> bottomImage.setImageResource(resId)
                    "신발"     -> shoesImage.setImageResource(resId)
                    "악세서리" -> accessoryImage.setImageResource(resId)
                    "헤어"     -> hairImage.setImageResource(resId)
                }
            },
            onBuy = { item ->
                AlertDialog.Builder(requireContext())
                    .setTitle("구매 확인")
                    .setMessage("${item.name}을(를) ${item.price}골드에 구매하시겠습니까?")
                    .setPositiveButton("구매") { _, _ ->
                        lifecycleScope.launch {
                            val userId = getUserIdFromPrefs() ?: return@launch
                            try {
                                RetrofitClient.shopApiService.purchaseItem(
                                    PurchaseRequest(userId, item.itemId, 1)
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "구매 성공!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadPurchasedItems(userId)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    "구매 실패: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        )

        binding.itemBox.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
            adapter = this@StoreFragment.adapter
        }

        listOf(
            binding.btnTop to 1,
            binding.btnBottom to 2,
            binding.btnShoes to 3,
            binding.btnAccessory to 4,
            binding.btnHair to 5
        ).forEach { (btn, categoryId) ->
            btn.setOnClickListener {
                highlightButton(it)
                loadItems(categoryId)
            }
        }

        // 초기 로딩
        val userId = getUserIdFromPrefs() ?: return
        loadPurchasedItems(userId)
        binding.btnTop.performClick()

        binding.btnBuyAll.setOnClickListener {
            val toBuy = if (previewedItems.isNotEmpty()) {
                previewedItems.toList()
            } else {
                val style = currentStyle ?: run {
                    Toast.makeText(
                        requireContext(),
                        "먼저 옷을 미리 입혀 주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                adapter.currentList.filter { item ->
                    when (item.categoryName) {
                        "상의"     -> item.nameEn == style.outfit
                        "하의"     -> item.nameEn == style.bottom
                        "신발"     -> item.nameEn == style.shoes
                        "악세서리" -> item.nameEn == style.accessory
                        "헤어"     -> item.nameEn == style.hair
                        else       -> false
                    }
                }
            }

            if (toBuy.isEmpty()) {
                Toast.makeText(requireContext(), "구매할 옷이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("일괄 구매 확인")
                .setMessage(
                    "총 ${toBuy.size}개의 옷을\n" +
                            "총 ${toBuy.sumOf { it.price }}골드에 구매하시겠습니까?"
                )
                .setPositiveButton("구매") { _, _ ->
                    lifecycleScope.launch {
                        toBuy.forEach { item ->
                            try {
                                val uid = getUserIdFromPrefs() ?: return@forEach
                                RetrofitClient.shopApiService.purchaseItem(
                                    PurchaseRequest(uid, item.itemId, 1)
                                )
                            } catch (e: Exception) {
                                Log.e("StoreFragment", "Batch purchase failed", e)
                            }
                        }
                        Toast.makeText(
                            requireContext(),
                            "옷 ${toBuy.size}개 구매 완료!",
                            Toast.LENGTH_SHORT
                        ).show()
                        previewedItems.clear()
                        loadPurchasedItems(userId)
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }

        loadCharacterStyle(userId)
    }

    private fun loadPurchasedItems(userId: String) {
        lifecycleScope.launch {
            try {
                // UserItem 리스트를 받음
                val userItems: List<UserItem> =
                    RetrofitClient.shopApiService.getUserItems(userId)

                // itemId(Int)만 추출해 Set<Int>로 변환
                purchasedIds = userItems.map { it.itemId }.toSet()

                // 어댑터에 전달
                adapter.setPurchasedItems(purchasedIds)

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "구매 내역 로드 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadItems(categoryId: Int) {
        lifecycleScope.launch {
            try {
                val items: List<Item> =
                    RetrofitClient.shopApiService.getItemsByCategory(categoryId)
                adapter.submitList(items)
                adapter.setPurchasedItems(purchasedIds)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "아이템 불러오기 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun highlightButton(selected: View) {
        listOf(
            binding.btnTop, binding.btnBottom, binding.btnShoes,
            binding.btnAccessory, binding.btnHair
        ).forEach { btn ->
            btn.alpha = if (btn == selected) 1f else 0.5f
        }
    }

    private fun getUserIdFromPrefs(): String? =
        requireContext()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("userId", null)

    private fun loadCharacterStyle(userId: String) {
        RetrofitClient.characterApi.getStyle(userId)
            .enqueue(object : Callback<CharacterStyleResponseDto> {
                override fun onResponse(
                    call: Call<CharacterStyleResponseDto>,
                    response: Response<CharacterStyleResponseDto>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            currentStyle = it
                            updateCharacterImages(it)
                        }
                    }
                }
                override fun onFailure(call: Call<CharacterStyleResponseDto>, t: Throwable) {
                    Log.e("StoreFragment", "스타일 로드 실패: ${t.message}")
                }
            })
    }

    private fun updateCharacterImages(style: CharacterStyleResponseDto) {
        style.hair?.let { hairImage.setImageResource(getDrawableResIdByName(it)) }
            ?: hairImage.setImageDrawable(null)
        style.outfit?.let { outfitImage.setImageResource(getDrawableResIdByName(it)) }
            ?: outfitImage.setImageDrawable(null)
        style.bottom?.let { bottomImage.setImageResource(getDrawableResIdByName(it)) }
            ?: bottomImage.setImageDrawable(null)
        style.accessory?.let { accessoryImage.setImageResource(getDrawableResIdByName(it)) }
            ?: accessoryImage.setImageDrawable(null)
        style.shoes?.let { shoesImage.setImageResource(getDrawableResIdByName(it)) }
            ?: shoesImage.setImageDrawable(null)
    }

    private fun getDrawableResIdByName(resourceName: String): Int =
        resources.getIdentifier(
            resourceName, "drawable", requireContext().packageName
        )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
