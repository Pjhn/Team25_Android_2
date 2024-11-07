package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.ManagerRegisterDto
import com.kakaotech.team25M.data.network.dto.PatchImageDto

interface ManagerRepository {
    suspend fun registerManager(managerRegisterDto: ManagerRegisterDto): Result<String>

    suspend fun getName(): String?

    suspend fun patchImage(patchImageDto: PatchImageDto): Result<String?>
}
