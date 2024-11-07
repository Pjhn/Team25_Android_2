package com.example.team25.domain.repository

import com.example.team25.domain.model.ReservationInfo
import com.example.team25.domain.model.ReservationStatus
import kotlinx.coroutines.flow.Flow


interface ReservationRepository {
    fun getReservationsFlow(): Flow<List<ReservationInfo>>

    suspend fun changeReservation(reservationId: String, reservationStatus: ReservationStatus)
}
