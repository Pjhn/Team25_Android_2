package com.kakaotech.team25M.data.network.dto.mapper

import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.domain.model.AccompanyInfo

object AccompanyMapper: DomainMapper<List<AccompanyInfo>, List<AccompanyDto>> {
    override fun asDomain(dto: List<AccompanyDto>): List<AccompanyInfo> {
        return dto.map { accompanyDto ->
            AccompanyInfo(
                status = accompanyDto.status,
                statusDate = accompanyDto.statusDate,
                statusDescribe = accompanyDto.statusDescribe
            )
        }
    }

    override fun asDto(domain: List<AccompanyInfo>): List<AccompanyDto> {
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
