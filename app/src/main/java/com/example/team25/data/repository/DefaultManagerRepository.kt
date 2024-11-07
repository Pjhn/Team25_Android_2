package com.example.team25.data.repository

import android.util.Log
import com.example.team25.data.network.dto.ManagerRegisterDto
import com.example.team25.data.network.dto.PatchImageDto
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
            Log.d(TAG, "Response: $response")
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d(TAG, "Response Body: $responseBody")
                if (responseBody != null && responseBody.status) {
                    Result.success(responseBody.message)
                } else {
                    Log.e(TAG, "Invalid response body or status is false")
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Log.e(TAG, "Registration failed with status code: ${response.code()}")
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getName(): String? {
        val response = managerService.getName()
        return if (response.isSuccessful) {
            response.body()?.data?.name
        } else {
            null
        }
    }

    override suspend fun patchImage(patchImageDto: PatchImageDto): Result<String?> {
        return try {
            val response = managerService.patchImage(patchImageDto)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status == true) {
                    Result.success(responseBody.message)
                } else {
                    Log.e(TAG, "Invalid response body or status is false")
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Log.e(TAG, "Image Patch failed with status code: ${response.code()}")
                Result.failure(Exception("Image Patch failed"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception occurred: ${e.message}", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ManagerRepository"
    }

}
