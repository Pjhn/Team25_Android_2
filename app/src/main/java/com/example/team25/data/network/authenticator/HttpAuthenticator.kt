package com.example.team25.data.network.authenticator


import android.util.Log
import androidx.datastore.core.DataStore
import com.example.team25.TokensProto.Tokens
import com.example.team25.data.network.dto.RefreshTokenDto
import com.example.team25.data.remote.SignIn
import com.example.team25.di.TokenDataStore
import com.example.team25.domain.repository.LoginRepository
import com.example.team25.exceptions.TokenExpiredException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class HttpAuthenticator @Inject constructor(
    @TokenDataStore private val tokenDataStore: DataStore<Tokens>,
    private val signIn: SignIn,
    private val loginRepository: LoginRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        return runBlocking {
            val refreshToken = getRefreshToken()
            if (refreshToken.isNullOrEmpty()) {
                Log.e("HttpAuthenticator", "리프레시 토큰이 없습니다.")
                throw TokenExpiredException("리프레시 토큰이 없습니다.")
            } else {
                val newAccessToken = refreshAccessToken(refreshToken)
                if (newAccessToken != null) {
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    Log.e("HttpAuthenticator", "토큰 갱신 실패")
                    throw TokenExpiredException("토큰 갱신 실패")
                }
            }
        }
    }

    private suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        tokenDataStore.data.first().refreshToken
    }

    private suspend fun refreshAccessToken(refreshToken: String): String? {
        return try {
            val refreshTokenDto = RefreshTokenDto(refreshToken)
            Log.d("HttpAuthenticator", "$refreshTokenDto")
            val response = signIn.refreshToken(refreshTokenDto)
            if (response.isSuccessful) {
                Log.d("HttpAuthenticator", "Response Body: ${response.body()}")
                response.body()?.let { tokenDto ->
                    loginRepository.saveTokens(
                        tokenDto.data.accessToken,
                        tokenDto.data.refreshToken,
                        tokenDto.data.expiresIn,
                        tokenDto.data.refreshTokenExpiresIn
                    )
                    tokenDto.data.accessToken
                }
            } else {
                Log.e("HttpAuthenticator", "Failed to refresh token - Response code: ${response.code()}, Message: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("HttpAuthenticator", "토큰 갱신 오류", e)
            null
        }
    }

}