package com.kakaotech.team25M.data.network.dto

import com.kakaotech.team25M.domain.model.ReservationStatus
import com.google.gson.annotations.SerializedName
import com.kakaotech.team25M.domain.model.Gender

data class ReservationDto(
    @SerializedName("managerId") val managerId: String,
    @SerializedName("reservationId") val reservationId: String,
    @SerializedName("reservationStatus") val reservationStatus: ReservationStatus,
    @SerializedName("departureLocation") val departureLocation: String,
    @SerializedName("arrivalLocation") val arrivalLocation: String,
    @SerializedName("reservationDateTime") val reservationDate: String,
    @SerializedName("serviceType") val serviceType: String,
    @SerializedName("transportation") val transportation: String,
    @SerializedName("price") val price: Int,
    @SerializedName("patient") val patient: Patient
)

data class Patient(
    @SerializedName("name") val name: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("patientGender") val patientGender: Gender?,
    @SerializedName("patientRelation") val patientRelation: String,
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("nokPhone") val nokPhone: String
)
