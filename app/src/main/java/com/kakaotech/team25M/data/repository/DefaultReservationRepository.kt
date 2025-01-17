package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.data.network.dto.mapper.asDomain
import com.kakaotech.team25M.data.network.services.ReservationService
import com.kakaotech.team25M.domain.model.ReservationInfo
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
            Log.d(TAG, "${response.body()}")

            val responseData = response.body()?.data
            val reservations = responseData.asDomain()

            emit(reservations)
        } else {
            emit(listOf())
            Log.e(TAG, "${response.code()}")
        }
    }

    override suspend fun changeReservation(reservationId: String, reservationStatusDto: ReservationStatusDto): Result<String> {
        val response = reservationService.changeReservation(reservationId, reservationStatusDto)
        return if (response.isSuccessful) {
            Log.d(TAG, "${response.body()}")
            Result.success("변경 성공")
        } else {
            Log.e(TAG, response.toString())
            Result.failure(Exception("Invalid response"))
        }
    }

    companion object {
        private const val TAG = "ReservationRepository"
    }
}
