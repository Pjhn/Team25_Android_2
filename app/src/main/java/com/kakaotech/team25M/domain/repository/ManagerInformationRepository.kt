package com.kakaotech.team25M.domain.repository

import com.kakaotech.team25M.data.network.dto.PatchImageDto
import com.kakaotech.team25M.data.network.dto.PatchLocationDto
import com.kakaotech.team25M.data.network.dto.ProfileDto

interface ManagerInformationRepository {

    suspend fun getProfile(): Result<ProfileDto?>

    suspend fun patchImage(patchImageDto: PatchImageDto): Result<String?>

    suspend fun patchLocation(patchLocationDto: PatchLocationDto): Result<String?>

}
