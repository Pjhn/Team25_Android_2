package com.kakaotech.team25M.domain.usecase

import com.kakaotech.team25M.domain.model.AccompanyInfo
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetAccompanyUseCase @Inject constructor(
    private val accompanyRepository: AccompanyRepository
) {
    suspend operator fun invoke(reservationId: String): List<AccompanyInfo>? {
        return accompanyRepository.getAccompanyFlow(reservationId).firstOrNull()
    }
}
