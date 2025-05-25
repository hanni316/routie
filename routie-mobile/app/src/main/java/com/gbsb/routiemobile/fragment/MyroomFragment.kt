package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.adapter.StoreAdapter
import com.gbsb.routiemobile.databinding.FragmentMyroomBinding
import com.gbsb.routiemobile.dto.CharacterStyleResponseDto
import com.gbsb.routiemobile.dto.CharacterStyleRequestDto
import com.gbsb.routiemobile.dto.UserItem
import com.gbsb.routiemobile.dto.Item
import com.gbsb.routiemobile.network.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyroomFragment : Fragment() {

    private var _binding: FragmentMyroomBinding? = null
    private val binding get() = _binding!!

    // 아바타 레이어
    private lateinit var bodyImage: ImageView
    private lateinit var hairImage: ImageView
    private lateinit var outfitImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var shoesImage: ImageView
    private lateinit var accessoryImage: ImageView

    // 바텀시트 + 어댑터
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var storeAdapter: StoreAdapter
    private var allPurchased = emptyList<Item>()

    private var selectedHair:     String? = null
    private var selectedOutfit:   String? = null
    private var selectedBottom:   String? = null
    private var selectedAccessory:String? = null
    private var selectedShoes:    String? = null

    private lateinit var ticketCountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 아바타 이미지 뷰
        bodyImage      = binding.bodyImage
        hairImage      = binding.hairImage
        outfitImage    = binding.outfitImage
        bottomImage    = binding.bottomImage
        shoesImage     = binding.shoesImage
        accessoryImage = binding.accessoryImage

        // 로그인된 userId로 캐릭터 스타일 불러오기
        getUserIdFromPrefs()?.let { uid ->
            RetrofitClient.characterApi.getStyle(uid)
                .enqueue(object : Callback<CharacterStyleResponseDto> {
                    override fun onResponse(
                        call: Call<CharacterStyleResponseDto>,
                        response: Response<CharacterStyleResponseDto>
                    ) {
                        response.body()?.let { updateCharacterImages(it) }
                            ?: Log.e("Myroom", "스타일 응답 Null")
                    }
                    override fun onFailure(call: Call<CharacterStyleResponseDto>, t: Throwable) {
                        Log.e("Myroom", "스타일 호출 실패", t)
                    }
                })

            ticketCountTextView = view.findViewById(R.id.text_ticket_count1)

            //user Id 확인 후 api 호출(가챠부분)
            val userId = getUserIdFromPrefs()
            if (userId != null) {
                loadTicketCount(userId)
            }

        }

        // 네비 바 버튼
        binding.btnShop.setOnClickListener {
            findNavController().navigate(R.id.action_myroomFragment_to_storeFragment)
        }
        binding.btnOut.setOnClickListener {
            findNavController().navigate(R.id.action_myroomFragment_to_MainFragment)
        }

        binding.bottomSheet.visibility = View.VISIBLE

        binding.bottomSheet.post {
            bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
                isHideable = true
                val screenH   = binding.root.height
                peekHeight    = (screenH * 0.4).toInt()
                state         = BottomSheetBehavior.STATE_HIDDEN
            }
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // slideOffset 을 0~1 사이로 클램핑
                    val safeOffset = slideOffset.coerceIn(0f, 1f)

                    // 원하는 최대 이동량 정의
                    val maxShift = binding.root.height * 0.2f

                    // 실제 이동량 계산
                    val shift = maxShift * safeOffset

                    // 캐릭터를 위로 translation
                    binding.characterContainer.translationY = -shift
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.characterContainer.translationY = 0f
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.characterContainer.translationY = -binding.root.height * 0.2f
                        }
                        else -> {}
                    }
                }
            })

            // 스토어 섹션 세팅
            setupStoreSection()

            //가챠 버튼 클릭 이벤트->가챠화면
            binding.gachaButton.setOnClickListener {
                    if (parentFragmentManager.findFragmentByTag("GachaDialog") == null) {
                        GachaDialogFragment().show(parentFragmentManager, "GachaDialog")
                    }
            }


            // 옷장 버튼 클릭 토글
            binding.btnWardrobe.setOnClickListener {
                if (allPurchased.isEmpty()) {
                    loadPurchasedItems()
                }
                bottomSheetBehavior.state = when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_HIDDEN ->
                        BottomSheetBehavior.STATE_COLLAPSED
                    else ->
                        BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
        binding.btnSaveStyle.setOnClickListener {
            val userId = getUserIdFromPrefs() ?: return@setOnClickListener

            val dto = CharacterStyleRequestDto(
                hair      = selectedHair,
                outfit    = selectedOutfit,
                bottom    = selectedBottom,
                accessory = selectedAccessory,
                shoes     = selectedShoes
            )
            RetrofitClient.characterApi
                .saveStyle(userId, dto)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "스타일이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {  // ← Call<Void>
                        Toast.makeText(requireContext(), "네트워크 오류", Toast.LENGTH_SHORT).show()
                        Log.e("Myroom", "saveStyle failed", t)
                    }
                })
        }

    }

        private fun setupStoreSection() {
        // 어댑터: onPreview만 사용
        storeAdapter = StoreAdapter(
            showPrice = false,
            onPreview = { applyPreview(it) },
            onBuy     = { /* 마이룸에서는 구매 안 함 */ }
        )

        // RecyclerView
        binding.rvStore.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
            adapter = storeAdapter
        }

        // 카테고리 버튼 누르면 해당 카테고리 구매 아이템만 필터링
        listOf(
            binding.btnTopStore    to "상의",
            binding.btnBottomStore to "하의",
            binding.btnShoesStore  to "신발",
            binding.btnAccessoryStore to "악세서리",
            binding.btnHairStore   to "헤어"
        ).forEach { (btn, catName) ->
            btn.setOnClickListener {
                // 버튼 강조
                listOf(
                    binding.btnTopStore,
                    binding.btnBottomStore,
                    binding.btnShoesStore,
                    binding.btnAccessoryStore,
                    binding.btnHairStore
                ).forEach { it.alpha = if (it == btn) 1f else 0.5f }

                // 필터링: allPurchased에서 categoryName 매칭
                storeAdapter.submitList(allPurchased.filter { it.categoryName == catName })
            }
        }

        // 바텀시트가 펼쳐질 때 한 번만 데이터를 불러오도록
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED && allPurchased.isEmpty()) {
                        loadPurchasedItems()
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }
        )
    }

    private fun loadPurchasedItems() {
        val userId = getUserIdFromPrefs() ?: return
        lifecycleScope.launch {
            try {
                // 서버 UserItem 리스트
                val userItems: List<UserItem> =
                    RetrofitClient.shopApiService.getUserItems(userId)

                // Item 데이터 클래스로 변환
                allPurchased = userItems.map { ui ->
                    Item(
                        itemId       = ui.itemId,
                        name         = ui.name,
                        nameEn       = ui.nameEn,
                        price        = ui.price,
                        categoryName = ui.categoryName
                    )
                }

                // 현재 선택된 카테고리에 맞춰 필터링 후 어댑터에 넘기기
                val currentCat = listOf(
                    "상의"      to binding.btnTopStore,
                    "하의"      to binding.btnBottomStore,
                    "신발"      to binding.btnShoesStore,
                    "악세서리"  to binding.btnAccessoryStore,
                    "헤어"      to binding.btnHairStore
                ).first { it.second.alpha == 1f }.first

                storeAdapter.submitList(
                    allPurchased.filter { it.categoryName == currentCat }
                )

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "구매 아이템 로드 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun applyPreview(item: Item) {
        val resId = resources.getIdentifier(
            item.nameEn, "drawable", requireContext().packageName
        )
        when (item.categoryName) {
            "상의" -> {
                outfitImage.setImageResource(resId)
                // ▶▶ 선택한 옷 상태 변수에 저장
                selectedOutfit = item.nameEn
            }
            "하의" -> {
                bottomImage.setImageResource(resId)
                selectedBottom = item.nameEn
            }
            "신발" -> {
                shoesImage.setImageResource(resId)
                selectedShoes = item.nameEn
            }
            "악세서리" -> {
                accessoryImage.setImageResource(resId)
                selectedAccessory = item.nameEn
            }
            "헤어" -> {
                hairImage.setImageResource(resId)
                selectedHair = item.nameEn
            }
        }
    }

    private fun getUserIdFromPrefs(): String? {
        return requireContext()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("userId", null)
    }

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

    private fun getDrawableResIdByName(resourceName: String): Int {
        return resources.getIdentifier(
            resourceName, "drawable", requireContext().packageName
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadTicketCount(userId: String) {
        RetrofitClient.ticketApi.getTicketCount(userId)
            .enqueue(object : Callback<com.gbsb.routiemobile.dto.TicketCountDto> {
                override fun onResponse(
                    call: Call<com.gbsb.routiemobile.dto.TicketCountDto>,
                    response: Response<com.gbsb.routiemobile.dto.TicketCountDto>
                ) {
                    if (response.isSuccessful) {
                        val count = response.body()?.ticketCount ?: 0
                        ticketCountTextView.text = "${count}장"
                    } else {
                        Log.w("Myroom", "티켓 수 조회 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<com.gbsb.routiemobile.dto.TicketCountDto>, t: Throwable) {
                    Log.e("Myroom", "티켓 수 조회 에러", t)
                }
            })
    }
}
