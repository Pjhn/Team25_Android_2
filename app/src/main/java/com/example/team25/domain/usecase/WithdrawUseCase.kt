package com.example.team25.domain.usecase

import com.example.team25.domain.repository.UserRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String? {
        return userRepository.withdraw()
    }
}
