package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.domain.model.AccompanyInfo
import kotlinx.coroutines.flow.Flow

interface AccompanyRepository {
    suspend fun postAccompanyInfo(reservationId: String, accompanyDto: AccompanyDto)

    fun getAccompanyFlow(reservationId: String): Flow<List<AccompanyInfo>>
}
