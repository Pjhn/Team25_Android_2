package com.kakaotech.team25M.data.network.services

import com.kakaotech.team25M.data.network.dto.DaySchedule
import com.kakaotech.team25M.data.network.dto.ManagerCommentRequest
import com.kakaotech.team25M.data.network.dto.ManagerCommentResponse
import com.kakaotech.team25M.data.network.dto.ManagerLocationRequest
import com.kakaotech.team25M.data.network.dto.ManagerLocationResponse
import com.kakaotech.team25M.data.network.dto.ManagerTimeResponse
import com.kakaotech.team25M.data.network.dto.ProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ManagerInformationService {

    @PATCH("/api/manager/comment")
    suspend fun changeComment(
        @Body commentRequest: ManagerCommentRequest
    ): Response<ManagerCommentResponse>

    @PATCH("/api/manager/location")
    suspend fun changeLocation(
        @Body locationRequest: ManagerLocationRequest
    ): Response<ManagerLocationResponse>

    @POST("/api/manager/time/")
    suspend fun updateManagerSchedule(
        @Body schedule: DaySchedule
    ): Response<ManagerTimeResponse>

    @GET("/api/manager/me/profile")
    suspend fun getProfile(): Response<ProfileDto?>
}
