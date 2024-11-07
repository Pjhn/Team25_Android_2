package com.example.team25.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class AccompanyInfo (
    val status: String,
    val statusDate: String,
    val statusDescribe: String = ""
): Parcelable
