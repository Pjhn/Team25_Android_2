package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.mapper.asDomain
import com.kakaotech.team25M.data.network.services.AccompanyService
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.model.AccompanyInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultAccompanyRepository @Inject constructor(
    private val accompanyService: AccompanyService,
) : AccompanyRepository {
    override suspend fun postAccompanyInfo(reservationId: String, accompanyDto: AccompanyDto) {
        val response = accompanyService.postAccompanyInfo(reservationId, accompanyDto)
        Log.d("testt", accompanyDto.toString())
        if (response.isSuccessful) {
            Log.d("AccompanyRepository", "${response.body()}")
        } else Log.e("AccompanyRepository", "${response.code()} ${response.message()}")
    }

    override fun getAccompanyFlow(reservationId: String): Flow<List<AccompanyInfo>> = flow {
        val response = accompanyService.getAccompanyInfo(reservationId)
        if (response.isSuccessful) {
            val accompanyLogs = response.body()?.data.asDomain()
            emit(accompanyLogs)
        } else {
            emit(listOf())
        }
    }
}
