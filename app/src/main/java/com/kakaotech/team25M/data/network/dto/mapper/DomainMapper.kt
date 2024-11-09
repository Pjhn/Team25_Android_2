package com.kakaotech.team25M.data.network.dto.mapper

interface DomainMapper<Domain, Dto> {
    fun asDto(domain: Domain): Dto

    fun asDomain(dto: Dto): Domain
}
