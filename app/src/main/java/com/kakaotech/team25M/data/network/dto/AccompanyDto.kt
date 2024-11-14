package com.kakaotech.team25M.data.network.dto

import com.google.gson.annotations.SerializedName

data class AccompanyDto (
    @SerializedName("status") val status: String,
    @SerializedName("statusDate") val statusDate: String,
    @SerializedName("statusDescribe") val statusDescribe: String = ""
)
