package com.kakaotech.team25M.domain.usecase

import com.kakaotech.team25M.data.network.dto.PatchTimeDto
import com.kakaotech.team25M.domain.model.WorkTimeDomain
import com.kakaotech.team25M.domain.model.toDto
import com.kakaotech.team25M.domain.repository.ManagerInformationRepository
import javax.inject.Inject

class PatchTimeUseCase @Inject constructor(
    private val managerInformationRepository: ManagerInformationRepository
) {
    suspend operator fun invoke(workTimeDomain: WorkTimeDomain): Result<String?> {
        return managerInformationRepository.patchTime(workTimeDomain.toDto())
    }
}
