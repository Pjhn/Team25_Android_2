package com.example.team25.domain.repository

import com.example.team25.data.network.dto.ManagerRegisterDto
import com.example.team25.data.network.dto.PatchImageDto

interface ManagerRepository {
    suspend fun registerManager(managerRegisterDto: ManagerRegisterDto): Result<String>

    suspend fun getName(): String?

    suspend fun patchImage(patchImageDto: PatchImageDto): Result<String?>
}
