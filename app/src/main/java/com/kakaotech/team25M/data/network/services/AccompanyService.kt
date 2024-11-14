package com.kakaotech.team25M.data.network.services

import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccompanyService {
    @POST("/api/manager/tracking/{reservation_id}")
    suspend fun postAccompanyInfo(
        @Path("reservation_id") reservationId: String,
        @Body accompanyDto: AccompanyDto
    ): Response<ServiceResponse<AccompanyDto>>

    @GET("/api/tracking/{reservation_id}")
    suspend fun getAccompanyInfo(
        @Path("reservation_id") reservationId: String,
    ): Response<ServiceResponse<List<AccompanyDto>>>
}
