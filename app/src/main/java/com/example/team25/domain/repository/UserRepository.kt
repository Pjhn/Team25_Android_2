package com.example.team25.domain.repository

import com.example.team25.data.network.dto.UserRole

interface UserRepository {
    suspend fun getUserRole(): UserRole?
}
