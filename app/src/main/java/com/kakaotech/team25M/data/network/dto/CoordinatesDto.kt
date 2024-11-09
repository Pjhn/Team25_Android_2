package com.kakaotech.team25M.data.network.dto

import com.google.gson.annotations.SerializedName

data class CoordinatesDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
