package com.kakaotech.team25M.data.network.services

import com.kakaotech.team25M.data.network.dto.ReservationDto
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.data.network.dto.ServiceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ReservationService {
    @GET("/api/reservations/manager")
    suspend fun getReservations(): Response<ServiceResponse<List<ReservationDto>>>

    @PATCH("/api/reservations/change/{reservation_id}")
    suspend fun changeReservation(
        @Path("reservation_id") reservationId: String,
        @Body reservationStatusDto: ReservationStatusDto
    ): Response<ServiceResponse<ReservationDto>>
}
