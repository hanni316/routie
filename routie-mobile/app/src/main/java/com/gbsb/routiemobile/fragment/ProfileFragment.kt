package com.gbsb.routiemobile.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gbsb.routiemobile.R
import com.gbsb.routiemobile.config.ServerConfig
import com.gbsb.routiemobile.dto.Achievement
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ProfileFragmentArgs by navArgs()
        val userId = args.userId
        val profileImageUrl = args.profileImageUrl
        val nickname = args.nickname
        Log.d("ProfileFragment", "받은 userId: $userId")

        val prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.getString("profile_image_url", null)

        val profileImageView = view.findViewById<ImageView>(R.id.profile_img)

        if (!profileImageUrl.isNullOrBlank()) {
            val fullUrl = "${ServerConfig.BASE_URL}$profileImageUrl"
            Log.d("프로필 이미지", "URL: $fullUrl")

            Glide.with(this)
                .load(fullUrl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .circleCrop()
                .into(profileImageView)
        }

        val nicknameTextView = view.findViewById<TextView>(R.id.textView6)
        nicknameTextView.text = nickname

        if (userId != null) {
            RetrofitClient.achievementApi.getUserAchievements(userId)
                .enqueue(object : Callback<List<Achievement>> {
                    override fun onResponse(call: Call<List<Achievement>>, response: Response<List<Achievement>>) {
                        Log.d("ProfileFragment", "응답 성공: ${response.code()}")
                        if (response.isSuccessful) {
                            val achievements = response.body() ?: emptyList()
                            updateAchievementsUI(achievements)
                        } else {
                            Log.e("ProfileFragment", "응답 실패: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<Achievement>>, t: Throwable) {
                        Log.e("ProfileFragment", "네트워크 오류: ${t.message}")
                    }
                })
        } else {
            Log.e("ProfileFragment", "userId가 null입니다!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun updateAchievementsUI(achievements: List<Achievement>) {
        achievements.forEach { achievement ->
            when (achievement.title) {
                "두 발 요정" -> {
                    val fairyMask = view?.findViewById<CardView>(R.id.fary_mask)
                    fairyMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "산책 괴물" -> {
                    val monsterMask = view?.findViewById<CardView>(R.id.monster_mask)
                    monsterMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "GPS 파괴자" -> {
                    val gpsMask = view?.findViewById<CardView>(R.id.gps_mask)
                    gpsMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "지갑오픈" -> {
                    val walletMask = view?.findViewById<CardView>(R.id.wallet_mask)
                    walletMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "결제 천사" -> {
                    val angelMask = view?.findViewById<CardView>(R.id.angel_mask)
                    angelMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "현타 마법사" -> {
                    val wizardMask = view?.findViewById<CardView>(R.id.wizard_mask)
                    wizardMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "지렸다...(축축)" -> {
                    val peeMask = view?.findViewById<CardView>(R.id.pee_mask)
                    peeMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }

                "운빨 X망겜" -> {
                    val loserMask = view?.findViewById<CardView>(R.id.loser_mask)
                    loserMask?.visibility = if (achievement.achieved) View.GONE else View.VISIBLE
                }
            }
        }
    }
}