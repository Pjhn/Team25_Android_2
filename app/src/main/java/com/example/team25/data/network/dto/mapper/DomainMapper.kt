package com.example.team25.data.network.dto.mapper

interface DomainMapper<Domain, Dto> {
    fun asDto(domain: Domain): Dto

    fun asDomain(dto: Dto): Domain
}
