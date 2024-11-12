package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.ReportDto
import com.kakaotech.team25M.data.network.services.ReportService
import com.kakaotech.team25M.domain.repository.ReportRepository
import javax.inject.Inject

class DefaultReportRepository @Inject constructor(
    private val reportService: ReportService
): ReportRepository {
    override suspend fun postReport(reservationId: String, reportDto: ReportDto) {
        val response = reportService.postReportInfo(reservationId, reportDto)
        if (response.isSuccessful) {
            Log.d("ReportRepository", "${response.body()}")
        } else Log.e("ReportRepository", "${response.code()} ${response.message()}")
    }
}
