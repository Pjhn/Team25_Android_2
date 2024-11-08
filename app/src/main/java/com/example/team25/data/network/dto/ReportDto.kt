package com.example.team25.data.network.dto

import com.google.gson.annotations.SerializedName

data class ReportDto(
    @SerializedName("doctorSummary") val doctorSummary: String?,
    @SerializedName("frequency") val frequency: Int?,
    @SerializedName("medicineTime") val medicineTime: String?,
    @SerializedName("timeOfDays") val timeOfDays: String?
)
