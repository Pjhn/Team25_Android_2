package com.example.team25.domain.repository

import com.example.team25.data.network.dto.DaySchedule
import com.example.team25.data.network.dto.ManagerCommentRequest
import com.example.team25.data.network.dto.ManagerCommentResponse
import com.example.team25.data.network.dto.ManagerLocationRequest
import com.example.team25.data.network.dto.ManagerLocationResponse
import com.example.team25.data.network.dto.ManagerTimeResponse
import com.example.team25.data.network.dto.ProfileDto

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

    suspend fun getProfile(): ProfileDto?
}
