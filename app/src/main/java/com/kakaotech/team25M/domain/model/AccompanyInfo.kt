package com.kakaotech.team25M.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccompanyInfo (
    val status: String,
    val statusDate: String,
    val statusDescribe: String = ""
): Parcelable
