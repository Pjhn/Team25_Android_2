package com.kakaotech.team25M.ui.main

sealed class WithdrawStatus {
    data object Idle : WithdrawStatus()
    data object Success : WithdrawStatus()
    data object Failure : WithdrawStatus()
}
