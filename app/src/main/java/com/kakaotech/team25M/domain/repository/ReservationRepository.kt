package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus
import kotlinx.coroutines.flow.Flow


interface ReservationRepository {
    fun getReservationsFlow(): Flow<List<ReservationInfo>>

    suspend fun changeReservation(reservationId: String, reservationStatus: ReservationStatus)
}
