package com.example.team25.data.network.services

import com.example.team25.data.network.dto.ReservationDto
import com.example.team25.data.network.dto.ServiceResponse
import com.example.team25.domain.model.ReservationStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ReservationService {
    @GET("/api/reservations")
    suspend fun getReservations(): Response<ServiceResponse<List<ReservationDto>>>

    @PATCH("/api/reservations/change/{reservation_id}")
    suspend fun changeReservation(
        @Path("reservation_id") reservationId: String,
        @Body reservationStatus: ReservationStatus
    ): Response<ServiceResponse<ReservationDto>>
}
