package com.kakaotech.team25M.data.network.dto

import com.google.gson.annotations.SerializedName

data class ReservationStatusDto (
    @SerializedName("reservationStatus") val reservationStatus: String,
)
