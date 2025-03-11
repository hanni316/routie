package com.gbsb.routiemobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gbsb.routiemobile.api.RewardResponse
import com.gbsb.routiemobile.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardViewModel : ViewModel() {

    private val _rewardData = MutableLiveData<RewardResponse>()
    val rewardData: LiveData<RewardResponse> get() = _rewardData

    fun fetchUserReward(userId: String) {
        RetrofitClient.api.getUserReward(userId).enqueue(object : Callback<RewardResponse> {
            override fun onResponse(call: Call<RewardResponse>, response: Response<RewardResponse>) {
                if (response.isSuccessful) {
                    _rewardData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<RewardResponse>, t: Throwable) {
                // 오류 처리
            }
        })
    }
}
