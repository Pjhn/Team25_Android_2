package com.kakaotech.team25M.data.network.services

import com.kakaotech.team25M.data.network.dto.PatchImageDto
import com.kakaotech.team25M.data.network.dto.PatchLocationDto
import com.kakaotech.team25M.data.network.dto.ProfileDto
import com.kakaotech.team25M.data.network.response.PatchImageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ManagerInformationService {
    @PATCH("/api/manager/image")
    suspend fun patchImage(
        @Body patchImageDto: PatchImageDto
    ): Response<PatchImageResponse>

    @PATCH("/api/manager/location")
    suspend fun patchLocation(
        @Body patchLocationDto: PatchLocationDto
    ): Response<PatchImageResponse>

    @GET("/api/manager/me/profile")
    suspend fun getProfile(): Response<ProfileDto?>
}
