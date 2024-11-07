package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.DaySchedule
import com.kakaotech.team25M.data.network.dto.ManagerCommentRequest
import com.kakaotech.team25M.data.network.dto.ManagerCommentResponse
import com.kakaotech.team25M.data.network.dto.ManagerLocationRequest
import com.kakaotech.team25M.data.network.dto.ManagerLocationResponse
import com.kakaotech.team25M.data.network.dto.ManagerTimeResponse
import com.kakaotech.team25M.data.network.dto.ProfileDto
import com.kakaotech.team25M.data.network.services.ManagerInformationService
import com.kakaotech.team25M.domain.repository.ManagerInformationRepository
import javax.inject.Inject

class DefaultManagerInformationRepository @Inject constructor(
    private val managerInformationService: ManagerInformationService
) : ManagerInformationRepository {
    override suspend fun changeManagerComment(
        commentRequest: ManagerCommentRequest
    ): Result<ManagerCommentResponse?> {
        return try {
            val response = managerInformationService.changeComment(commentRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status == true) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Change Comment failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun changeManagerLocation(
        locationRequest: ManagerLocationRequest
    ): Result<ManagerLocationResponse?> {
        return try {
            val response = managerInformationService.changeLocation(locationRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status == true) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Change Manager failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<ProfileDto?> {
        return try {
            val response = managerInformationService.getProfile()
            Log.d("profile", response.body().toString())
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status == true) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Get profile failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateManagerSchedule(schedule: DaySchedule): Result<ManagerTimeResponse?> {
        return try {
            val response = managerInformationService.updateManagerSchedule(schedule)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.status == true) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("change Schedule failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
