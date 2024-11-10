package com.kakaotech.team25M.ui.status.interfaces

import com.kakaotech.team25M.domain.model.ReservationInfo

interface OnCompanionStartClickListener {
    fun onStartClicked(reservationInfo: ReservationInfo)
    fun onCompleteClicked(reservationInfo: ReservationInfo)
}
