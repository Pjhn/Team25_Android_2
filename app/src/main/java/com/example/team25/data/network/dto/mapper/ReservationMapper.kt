package com.example.team25.data.network.dto.mapper

import com.example.team25.data.network.dto.ReservationDto
import com.example.team25.domain.model.Patient
import com.example.team25.domain.model.ReservationInfo

object ReservationMapper {
    fun asDomain(dto: List<ReservationDto>): List<ReservationInfo> {
        return dto.map { reservationDto ->
            ReservationInfo(
                managerId = reservationDto.managerId,
                reservationId = reservationDto.reservationId,
                reservationStatus = reservationDto.reservationStatus,
                departureLocation = reservationDto.departureLocation,
                arrivalLocation = reservationDto.arrivalLocation,
                reservationDate = reservationDto.reservationDate,
                serviceType = reservationDto.serviceType,
                transportation = reservationDto.transportation,
                price = reservationDto.price,
                patient = Patient(
                    patientName = reservationDto.patient.name,
                    patientPhone = reservationDto.patient.phoneNumber,
                    patientBirth = reservationDto.patient.birthDate,
                    patientGender = reservationDto.patient.patientGender,
                    nokPhone = reservationDto.patient.nokPhone
                    )
            )
        }
    }
}

fun List<ReservationDto>?.asDomain(): List<ReservationInfo> {
    return ReservationMapper.asDomain(this.orEmpty())
}
