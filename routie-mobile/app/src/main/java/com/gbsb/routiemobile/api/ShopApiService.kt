package com.gbsb.routiemobile.api

import com.gbsb.routiemobile.dto.Item
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopApiService {

    //카테고리별 아이템 조회
    @GET("/api/items/category/{categoryId}")
    suspend fun getItemsByCategory(
        @Path("categoryId") categoryId: Int
    ): List<Item>
}