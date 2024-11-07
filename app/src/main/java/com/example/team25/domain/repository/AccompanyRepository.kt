package com.example.team25.domain.repository

import com.example.team25.data.network.dto.AccompanyDto
import com.example.team25.domain.model.AccompanyInfo
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow

interface AccompanyRepository {
    suspend fun postAccompanyInfo(reservationId: String, accompanyDto: AccompanyDto)

    fun getAccompanyFlow(reservationId: String): Flow<List<AccompanyInfo>>
}
