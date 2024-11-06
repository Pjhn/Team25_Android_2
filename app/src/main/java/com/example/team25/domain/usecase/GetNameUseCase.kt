package com.example.team25.domain.usecase

import com.example.team25.domain.repository.ManagerRepository
import javax.inject.Inject

class GetNameUseCase @Inject constructor(
    private val managerRepository: ManagerRepository
) {
    suspend operator fun invoke(): String? {
        return managerRepository.getName()
    }
}
