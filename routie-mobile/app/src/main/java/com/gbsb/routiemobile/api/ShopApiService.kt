package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Item
import com.gbsb.routiemobile.dto.UserItem
import com.gbsb.routiemobile.dto.PurchaseRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Body

interface ShopApiService {

    //카테고리별 아이템 조회
    @GET("/api/items/category/{categoryId}")
    suspend fun getItemsByCategory(
        @Path("categoryId") categoryId: Int
    ): List<Item>

    @GET("/api/items/gacha")
    fun getGachaItems(): Call<List<Item>>

    //아이템 구매
    @POST("/api/shop/purchase")
    suspend fun purchaseItem(
        @Body request: PurchaseRequest
    ): String

    @GET("/api/user/items/{userId}")
    suspend fun getUserItems(@Path("userId") userId: String): List<UserItem>
}