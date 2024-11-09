package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.ReportDto

interface ReportRepository {
    suspend fun postReport(reservationId: String, reportDto: ReportDto)
}
