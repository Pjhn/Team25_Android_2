package com.kakaotech.team25M.domain.usecase

import com.kakaotech.team25M.data.network.dto.PatchImageDto
import com.kakaotech.team25M.domain.repository.ManagerInformationRepository
import com.kakaotech.team25M.domain.repository.ManagerRepository
import javax.inject.Inject

class PatchImageUseCase @Inject constructor(
    private val managerInformationRepository: ManagerInformationRepository
) {
    suspend operator fun invoke(newProfileImageUrl: String): Result<String?> {
        return managerInformationRepository.patchImage(PatchImageDto(newProfileImageUrl))
    }
}
