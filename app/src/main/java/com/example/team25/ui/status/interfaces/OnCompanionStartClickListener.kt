package com.example.team25.ui.status.interfaces

import com.example.team25.domain.model.AccompanyInfo
import com.example.team25.domain.model.ReservationInfo

interface OnCompanionStartClickListener {
    fun onStartClicked(reservationInfo: ReservationInfo)
    fun onCompleteClicked(reservationInfo: ReservationInfo)
}
