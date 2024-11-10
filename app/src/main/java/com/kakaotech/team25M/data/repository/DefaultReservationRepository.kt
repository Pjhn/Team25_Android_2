package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.mapper.asDomain
import com.kakaotech.team25M.data.network.services.ReservationService
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultReservationRepository @Inject constructor(
    private val reservationService: ReservationService
) : ReservationRepository {
    override fun getReservationsFlow(): Flow<List<ReservationInfo>> = flow {
        val response = reservationService.getReservations()
        if (response.isSuccessful) {
            val responseData = response.body()?.data
            val reservations = responseData.asDomain()

            emit(reservations)
        }
    }

    override suspend fun changeReservation(reservationId: String, reservationStatus: ReservationStatus) {
        val response = reservationService.changeReservation(reservationId, reservationStatus)
        if (response.isSuccessful) {
            Log.d("AccompanyRepository", "${response.body()}")
        } else Log.e("AccompanyRepository", "${response.code()}")
    }
}
