package com.example.team25.data.repository

import android.util.Log
import com.example.team25.data.network.dto.ReportDto
import com.example.team25.data.network.services.ReportService
import com.example.team25.domain.repository.ReportRepository
import javax.inject.Inject

class DefaultReportRepository @Inject constructor(
    private val reportService: ReportService
): ReportRepository {
    override suspend fun postReport(reservationId: String, reportDto: ReportDto) {
        val response = reportService.postReportInfo(reservationId, reportDto)
        if (response.isSuccessful) {
            Log.d("AccompanyRepository", "${response.body()}")
        } else Log.e("AccompanyRepository", "${response.code()}")
    }
}
