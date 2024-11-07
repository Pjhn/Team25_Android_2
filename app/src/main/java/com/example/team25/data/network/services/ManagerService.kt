package com.example.team25.data.network.services

import com.example.team25.data.network.dto.ManagerRegisterDto
import com.example.team25.data.network.dto.NameDto
import com.example.team25.data.network.dto.PatchImageDto
import com.example.team25.data.network.response.PatchImageResponse
import com.example.team25.data.network.response.RegisterManagerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ManagerService {
    @POST("/api/manager")
    suspend fun registerManager(
        @Body managerRegisterDto: ManagerRegisterDto
    ): Response<RegisterManagerResponse>

    @GET("/api/manager/name")
    suspend fun getName(): Response<NameDto>

    @PATCH("/api/manager/image")
    suspend fun patchImage(
        @Body pathImageDto: PatchImageDto
    ): Response<PatchImageResponse>
}
