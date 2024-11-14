package com.kakaotech.team25M.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.di.IoDispatcher
import com.kakaotech.team25M.domain.model.AccompanyInfo
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.usecase.GetAccompanyUseCase
import com.kakaotech.team25M.domain.usecase.GetNameUseCase
import com.kakaotech.team25M.domain.usecase.GetReservationsUseCase
import com.kakaotech.team25M.domain.usecase.LogoutUseCase
import com.kakaotech.team25M.domain.usecase.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val getNameUseCase: GetNameUseCase,
    private val getAccompanyUseCase: GetAccompanyUseCase,
    private val getReservationsUseCase: GetReservationsUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel(){
    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _withdrawEvent = MutableStateFlow<WithdrawStatus>(WithdrawStatus.Idle)
    val withdrawEvent: StateFlow<WithdrawStatus> = _withdrawEvent

    private val _reservations = MutableStateFlow<List<ReservationInfo>?>(null)
    val reservations: StateFlow<List<ReservationInfo>?> = _reservations

    init {
        updateReservations()
    }

    fun logout() {
        viewModelScope.launch(ioDispatcher) {
            logoutUseCase()
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            val withdrawResult = withdrawUseCase()
            _withdrawEvent.value = if (withdrawResult != null) {
                WithdrawStatus.Success
            } else {
                WithdrawStatus.Failure
            }
        }
    }

    fun getName() {
        viewModelScope.launch {
            val managerName = getNameUseCase()
            if (managerName != null) {
                _name.value = managerName
            }
        }
    }

    fun updateReservations() {
        viewModelScope.launch {
            _reservations.value = getReservationsUseCase.invoke()
        }
    }
}
