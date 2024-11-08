package com.example.team25.data.network.services

import com.example.team25.data.network.dto.CoordinatesDto
import com.example.team25.data.network.dto.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CoordinatesService {
    @POST("/api/manager/tracking/{reservation_id}/location")
    suspend fun postCoordinates(
        @Path("reservation_id") reservationId: String,
        @Body coordinatesDto: CoordinatesDto
    ): Response<ServiceResponse<CoordinatesDto>>

    @GET("/api/tracking/{reservation_id}/location")
    suspend fun getCoordinates(
        @Path("reservation_id") reservationId: String
    ): Response<ServiceResponse<CoordinatesDto>>
}
