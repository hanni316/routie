package com.gbsb.routiemobile.api

import retrofit2.Call
import com.gbsb.routiemobile.dto.TicketCountDto
import com.gbsb.routiemobile.dto.TicketUseRequestDto
import retrofit2.http.*

interface TicketApi {
    //보유 티켓 수 조회
    @GET("api/tickets/{userId}/count")
    fun getTicketCount(@Path("userId") userId: String): Call<TicketCountDto>

    //티켓 사용->감소
    @POST("/api/tickets/use")
    fun useTicket(@Body request: TicketUseRequestDto): Call<Void>
}