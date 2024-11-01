package com.example.team25.data.network.services

import com.example.team25.data.network.dto.UserRoleDto
import retrofit2.http.GET
import retrofit2.Response

interface UserService {
    @GET("/api/users/me/role")
    suspend fun getUserRole(): Response<UserRoleDto>
}

