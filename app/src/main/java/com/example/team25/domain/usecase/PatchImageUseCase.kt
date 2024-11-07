package com.example.team25.domain.usecase

import com.example.team25.data.network.dto.PatchImageDto
import com.example.team25.domain.repository.ManagerRepository
import javax.inject.Inject

class PatchImageUseCase @Inject constructor(
    private val managerRepository: ManagerRepository
) {
    suspend operator fun invoke(patchImageDto: PatchImageDto): Result<String?> {
        return managerRepository.patchImage(patchImageDto)
    }
}
