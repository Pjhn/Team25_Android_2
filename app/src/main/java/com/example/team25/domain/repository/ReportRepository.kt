package com.example.team25.domain.repository

import com.example.team25.data.network.dto.ReportDto

interface ReportRepository {
    suspend fun postReport(reservationId: String, reportDto: ReportDto)
}
