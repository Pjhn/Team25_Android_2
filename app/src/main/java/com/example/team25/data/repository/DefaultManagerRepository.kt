package com.example.team25.data.repository

import android.util.Log
import com.example.team25.data.network.dto.ManagerRegisterDto
import com.example.team25.data.network.services.ManagerService
import com.example.team25.domain.repository.ManagerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DefaultManagerRepository @Inject constructor(
    private val managerService: ManagerService,
) : ManagerRepository {

    override suspend fun registerManager(managerRegisterDto: ManagerRegisterDto): Result<String> {
        return try {
            val response = managerService.registerManager(managerRegisterDto)
            Log.d("ManagerRepository", "Response: $response")
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("ManagerRepository", "Response Body: $responseBody")
                if (responseBody != null && responseBody.status) {
                    Result.success(responseBody.message)
                } else {
                    Log.e("ManagerRepository", "Invalid response body or status is false")
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Log.e("ManagerRepository", "Registration failed with status code: ${response.code()}")
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Log.e("ManagerRepository", "Exception occurred: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getName(): String? {
        val response = managerService.getName()
        return if (response.isSuccessful) {
            Log.d("name", response.toString())
            response.body()?.data?.name
        } else {
            null
        }
    }

}
