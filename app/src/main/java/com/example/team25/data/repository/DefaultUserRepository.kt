package com.example.team25.data.repository

import com.example.team25.data.network.dto.UserRole
import com.example.team25.data.network.services.UserService
import com.example.team25.domain.repository.UserRepository
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override suspend fun getUserRole(): UserRole? {
        val response = userService.getUserRole()
        return if (response.isSuccessful) {
            response.body()?.data
        } else {
            null
        }
    }
}
