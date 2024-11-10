package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.CoordinatesDto
import com.kakaotech.team25M.data.network.services.CoordinatesService
import com.kakaotech.team25M.domain.repository.CoordinatesRepository
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultCoordinatesRepository @Inject constructor(
    private val coordinatesService: CoordinatesService
) : CoordinatesRepository {
    override suspend fun postCoordinates(reservationId: String, coordinatesDto: CoordinatesDto) {
        val response = coordinatesService.postCoordinates(reservationId, coordinatesDto)
        if (response.isSuccessful) {
            Log.d("CoordinatesRepository", "${response.body()}")
        } else Log.e("CoordinatesRepository", "${response.code()}")
    }

    override fun getCoordinatesFlow(reservationId: String): Flow<LatLng> = flow {
        val response = coordinatesService.getCoordinates(reservationId)
        if (response.isSuccessful) {
            val coordinates = response.body()?.data
            coordinates?.let { emit(LatLng.from(it.latitude, it.longitude)) }
        }
    }
}
