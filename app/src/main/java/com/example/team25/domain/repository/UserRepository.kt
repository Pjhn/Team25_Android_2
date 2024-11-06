package com.example.team25.domain.repository

import com.example.team25.data.network.dto.UserStatus

interface UserRepository {
    suspend fun getUserStatus(): UserStatus?

    suspend fun withdraw(): String?
}
