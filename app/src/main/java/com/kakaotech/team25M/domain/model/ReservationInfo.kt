package com.kakaotech.team25M.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservationInfo(
    val managerId: String = "",
    val reservationId: String = "",
    val reservationStatus: ReservationStatus = ReservationStatus.보류,
    val departureLocation: String = "",
    val arrivalLocation: String = "",
    val reservationDate: String = "",
    val serviceType: String = "",
    val transportation: String = "",
    val price: Int = 0,
    val patient: Patient = Patient()
) : Parcelable
