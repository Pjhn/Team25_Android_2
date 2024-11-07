package com.example.team25.domain.repository

import com.example.team25.data.network.dto.CoordinatesDto
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow

interface CoordinatesRepository {
    suspend fun postCoordinates(reservationId: String, coordinatesDto: CoordinatesDto)
    fun getCoordinatesFlow(reservationId: String): Flow<LatLng>
}
