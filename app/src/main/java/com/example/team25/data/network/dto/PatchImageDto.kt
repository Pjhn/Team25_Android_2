package com.example.team25.data.network.dto

import com.google.gson.annotations.SerializedName

data class PatchImageDto (
    @SerializedName("profileImage") val newProfileImage: String,
)
