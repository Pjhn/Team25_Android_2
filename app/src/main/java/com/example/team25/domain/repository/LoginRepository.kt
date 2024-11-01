package com.example.team25.domain.repository

import com.example.team25.data.network.dto.TokenDto
import com.example.team25.TokensProto.Tokens

interface LoginRepository {
    suspend fun login(oauthAccessToken: String): TokenDto?

    suspend fun saveTokens(accessToken: String, refreshToken: String, accessTokenExpiresIn: Long, refreshTokenExpiresIn: Long)

    suspend fun getSavedTokens(): Tokens?

    suspend fun logout()

}
