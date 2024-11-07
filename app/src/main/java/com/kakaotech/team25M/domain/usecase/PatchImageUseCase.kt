package com.kakaotech.team25M.domain.usecase

import com.kakaotech.team25M.data.network.dto.PatchImageDto
import com.kakaotech.team25M.domain.repository.ManagerRepository
import javax.inject.Inject

class PatchImageUseCase @Inject constructor(
    private val managerRepository: ManagerRepository
) {
    suspend operator fun invoke(patchImageDto: PatchImageDto): Result<String?> {
        return managerRepository.patchImage(patchImageDto)
    }
}
