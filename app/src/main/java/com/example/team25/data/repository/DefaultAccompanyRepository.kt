package com.example.team25.data.repository

import android.util.Log
import com.example.team25.data.network.dto.AccompanyDto
import com.example.team25.data.network.dto.mapper.asDomain
import com.example.team25.data.network.services.AccompanyService
import com.example.team25.data.network.services.CoordinatesService
import com.example.team25.domain.model.AccompanyInfo
import com.example.team25.domain.repository.AccompanyRepository
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultAccompanyRepository @Inject constructor(
    private val accompanyService: AccompanyService,
) : AccompanyRepository {
    override suspend fun postAccompanyInfo(reservationId: String, accompanyDto: AccompanyDto) {
        val response = accompanyService.postAccompanyInfo(reservationId, accompanyDto)
        if (response.isSuccessful) {
            Log.d("AccompanyRepository", "${response.body()}")
        } else Log.e("AccompanyRepository", "${response.code()}")
    }

    override fun getAccompanyFlow(reservationId: String): Flow<List<AccompanyInfo>> = flow {
        val response = accompanyService.getAccompanyInfo(reservationId)
        if (response.isSuccessful) {
            val accompanyLogs = response.body()?.data.asDomain()
            emit(accompanyLogs)
        }
    }
}
