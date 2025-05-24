package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.config.ServerConfig
import com.gbsb.routiemobile.databinding.FragmentRankingBinding
import com.gbsb.routiemobile.dto.RankingItem
import com.gbsb.routiemobile.network.RetrofitClient
import kotlinx.coroutines.launch

class RankingFragment : Fragment() {

    private lateinit var binding: FragmentRankingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        if (userId == null) {
            Log.e("RANKING", "로그인된 사용자 ID가 없습니다.")
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.rankingApi.getRanking(userId)
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        updateRankingUI(result.ranking.take(10))
                        Log.d("RANKING", "내 순위: ${result.myRank}")
                    }
                } else {
                    Log.e("RANKING", "응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RANKING", "네트워크 오류: ${e.message}")
            }
        }
    }

    private fun updateRankingUI(rankingList: List<RankingItem>) {
        for (i in rankingList.indices) {
            val item = rankingList[i]
            val nameViewId = resources.getIdentifier("rk_${i + 1}_name", "id", requireContext().packageName)
            val profileViewId = resources.getIdentifier("rk_${i + 1}_profile", "id", requireContext().packageName)
            val frameLayoutId = resources.getIdentifier("rk_${i + 1}", "id", requireContext().packageName)

            val nameTextView = view?.findViewById<TextView>(nameViewId)
            val profileImageView = view?.findViewById<ImageView>(profileViewId)
            val frameLayout = view?.findViewById<View>(frameLayoutId)

            nameTextView?.text = item.nickname

            Glide.with(requireContext())
                .load("${ServerConfig.BASE_URL}${item.profileImageUrl}")
                .placeholder(R.drawable.default_profile)
                .circleCrop()
                .into(profileImageView!!)

            frameLayout?.setOnClickListener {
                Log.e("CHECK", "ProfileFragment로 이동 시도 - userId: ${item.userId}")
                val action = RankingFragmentDirections.actionRankingFragmentToProfileFragment(
                    item.userId,
                    item.profileImageUrl ?: "",
                    item.nickname
                    )
                findNavController().navigate(action)
            }
        }
    }
}