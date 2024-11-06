package com.example.team25.domain.usecase

import com.example.team25.data.network.dto.ProfileDto
import com.example.team25.domain.repository.ManagerInformationRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val managerInformationRepository: ManagerInformationRepository
) {
    suspend operator fun invoke(): ProfileDto? {
        return try {
            val result = managerInformationRepository.getProfile()
            result.getOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
