package com.example.team25.data.repository

import com.example.team25.data.network.dto.ManagerCommentRequest
import com.example.team25.data.network.dto.ManagerCommentResponse
import com.example.team25.data.network.dto.ManagerLocationRequest
import com.example.team25.data.network.dto.ManagerLocationResponse
import com.example.team25.data.network.dto.ManagerTimeResponse
import com.example.team25.data.network.services.ManagerInformationService
import com.example.team25.dto.DaySchedule
import retrofit2.HttpException
import javax.inject.Inject

class DefaultManagerInformationRepository @Inject constructor(
    private val managerInformationService: ManagerInformationService
) {
    suspend fun changeManagerComment(
        managerId: String,
        commentRequest: ManagerCommentRequest
    ): Result<ManagerCommentResponse?> {
        return try {
            val response = managerInformationService.changeComment(managerId, commentRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status!!) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun changeManagerLocation(
        managerId: String,
        locationRequest: ManagerLocationRequest
    ): Result<ManagerLocationResponse?> {
        return try {
            val response = managerInformationService.changeLocation(managerId, locationRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status!!) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerManagerSchedule(managerId: String, schedule: DaySchedule): Result<ManagerTimeResponse?> {
        return try {
            val response = managerInformationService.registerManagerSchedule(managerId, schedule)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status!!) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateManagerSchedule(managerId: String, schedule: DaySchedule): Result<ManagerTimeResponse?> {
        return try {
            val response = managerInformationService.updateManagerSchedule(managerId, schedule)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status!!) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
