package com.example.team25.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.team25.TokensProto.Tokens
import com.example.team25.data.network.authenticator.HttpAuthenticator
import com.example.team25.data.network.dto.TokenDto
import com.example.team25.di.IoDispatcher
import com.example.team25.domain.model.UserRole
import com.example.team25.domain.usecase.GetSavedTokensUseCase
import com.example.team25.domain.usecase.GetUserRoleUseCase
import com.example.team25.domain.usecase.LoginUseCase
import com.example.team25.domain.usecase.LogoutUseCase
import com.example.team25.exceptions.TokenExpiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getSavedTokensUseCase: GetSavedTokensUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState


    fun login(oauthAccessToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val tokenDto: TokenDto? = loginUseCase(oauthAccessToken)
            if (tokenDto != null) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("로그인 실패")
            }
        }
    }

    fun updateErrorMessage(message: String) {
        _loginState.update { currentState ->
            if (currentState is LoginState.Error) {
                currentState.copy(message = message)
            } else {
                LoginState.Error(message)
            }
        }
    }

    suspend fun getSavedTokens(): Tokens? {
        return withContext(ioDispatcher) {
            getSavedTokensUseCase()
        }
    }

    fun logout() {
        viewModelScope.launch(ioDispatcher) {
            logoutUseCase()
            _loginState.value = LoginState.Idle
        }
    }

    suspend fun getUserRole(): UserRole? {
        return try {
            val roleString = getUserRoleUseCase()
            roleString?.let { UserRole.valueOf(it) }
        } catch (e: Exception) {
            Log.d("testt", e.message.toString())
            null
        }
    }
}
