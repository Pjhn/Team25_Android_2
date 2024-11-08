package com.example.team25.data.network.services

import com.example.team25.data.network.dto.ReportDto
import com.example.team25.data.network.dto.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportService {
    @POST("/api/reports/{reservation_id}")
    suspend fun postReportInfo(
        @Path("reservation_id") reservationId: String,
        @Body reportDto: ReportDto
    ): Response<ServiceResponse<ReportDto>>
}
