package com.example.team25.data.repository

import android.util.Log
import com.example.team25.data.network.dto.UserStatus
import com.example.team25.data.network.services.UserService
import com.example.team25.domain.repository.UserRepository
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val userService: UserService
) : UserRepository {
    override suspend fun getUserStatus(): UserStatus? {
        val response = userService.getUserStatus()
        return if (response.isSuccessful) {
            response.body()?.data
        } else {
            null
        }
    }

    override suspend fun withdraw(): String? {
        val response = userService.withdraw()
        return if (response.isSuccessful) {
            response.body()?.message
        } else {
            null
        }
    }
}
