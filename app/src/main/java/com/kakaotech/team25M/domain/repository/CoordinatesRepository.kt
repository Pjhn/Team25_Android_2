package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.CoordinatesDto
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow

interface CoordinatesRepository {
    suspend fun postCoordinates(reservationId: String, coordinatesDto: CoordinatesDto)
    fun getCoordinatesFlow(reservationId: String): Flow<LatLng>
}
