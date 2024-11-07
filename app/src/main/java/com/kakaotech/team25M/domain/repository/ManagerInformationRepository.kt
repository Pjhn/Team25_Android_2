package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.DaySchedule
import com.kakaotech.team25M.data.network.dto.ManagerCommentRequest
import com.kakaotech.team25M.data.network.dto.ManagerCommentResponse
import com.kakaotech.team25M.data.network.dto.ManagerLocationRequest
import com.kakaotech.team25M.data.network.dto.ManagerLocationResponse
import com.kakaotech.team25M.data.network.dto.ManagerTimeResponse
import com.kakaotech.team25M.data.network.dto.ProfileDto

interface ManagerInformationRepository {
    suspend fun changeManagerComment(
        commentRequest: ManagerCommentRequest
    ): Result<ManagerCommentResponse?>

    suspend fun changeManagerLocation(
        locationRequest: ManagerLocationRequest
    ): Result<ManagerLocationResponse?>

    suspend fun updateManagerSchedule(
        schedule: DaySchedule
    ): Result<ManagerTimeResponse?>

    suspend fun getProfile(): Result<ProfileDto?>
}
