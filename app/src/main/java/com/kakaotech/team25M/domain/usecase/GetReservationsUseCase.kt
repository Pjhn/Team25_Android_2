package com.kakaotech.team25M.domain.usecase

import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetReservationsUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(): List<ReservationInfo>? {
        return reservationRepository.getReservationsFlow().firstOrNull()
    }
}
