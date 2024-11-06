package com.example.team25.data.network.services

import com.example.team25.data.network.dto.UserStatusDto
import com.example.team25.data.network.dto.WithdrawDto
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.DELETE

interface UserService {
    @GET("/api/users/me/status")
    suspend fun getUserStatus(): Response<UserStatusDto>

    @DELETE("/api/users/withdraw")
    suspend fun withdraw(): Response<WithdrawDto>
}

