package com.example.team25.data.network.dto.mapper

import com.example.team25.data.network.dto.AccompanyDto
import com.example.team25.domain.model.AccompanyInfo

object AccompanyMapper {
    fun asDomain(dto: List<AccompanyDto>): List<AccompanyInfo> {
        return dto.map { accompanyDto ->
            AccompanyInfo(
                status = accompanyDto.status,
                statusDate = accompanyDto.statusDate,
                statusDescribe = accompanyDto.statusDescribe
            )
        }
    }

    fun asDto(domain: List<AccompanyInfo>): List<AccompanyDto> {
        return domain.map { accompanyDomain ->
            AccompanyDto(
                status = accompanyDomain.status,
                statusDate = accompanyDomain.statusDate,
                statusDescribe = accompanyDomain.statusDescribe
            )
        }
    }
}

fun List<AccompanyDto>?.asDomain(): List<AccompanyInfo> {
    return AccompanyMapper.asDomain(this.orEmpty())
}

fun List<AccompanyInfo>?.asDto(): List<AccompanyDto> {
    return AccompanyMapper.asDto(this.orEmpty())
}
