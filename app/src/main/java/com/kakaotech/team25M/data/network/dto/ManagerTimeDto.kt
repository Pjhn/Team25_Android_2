package com.kakaotech.team25M.data.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ManagerTimeResponse(
    @SerializedName("data") val data: ManagerTimeResult?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: Boolean?
)

data class ManagerTimeResult(
    @SerializedName("daySchedule") val daySchedule: DaySchedule?
)

@Parcelize
data class DaySchedule(
    var monStartTime : String = "00:00",
    var monEndTime : String = "00:00",
    var tueStartTime : String = "00:00",
    var tueEndTime : String = "00:00",
    var wedStartTime : String = "00:00",
    var wedEndTime : String = "00:00",
    var thuStartTime : String = "00:00",
    var thuEndTime : String = "00:00",
    var friStartTime : String = "00:00",
    var friEndTime : String = "00:00",
    var satStartTime : String = "00:00",
    var satEndTime : String = "00:00",
    var sunStartTime : String = "00:00",
    var sunEndTime : String = "00:00",
) : Parcelable
