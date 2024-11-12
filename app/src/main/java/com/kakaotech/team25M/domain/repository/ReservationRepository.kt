package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.domain.model.ReservationInfo
import kotlinx.coroutines.flow.Flow


interface ReservationRepository {
    fun getReservationsFlow(): Flow<List<ReservationInfo>>

    suspend fun changeReservation(reservationId: String, reservationStatusDto: ReservationStatusDto): Result<String>
}
