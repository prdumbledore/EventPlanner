package com.eriksargsyan.eventplanner.util.mappers

import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.network.CityNameNet
import java.util.*

class NetworkCityNameMapper : EntityToDomainMapper<CityNameNet, CityName> {

    override fun entityToDomainMap(entity: CityNameNet) = CityName(

        name = entity.name,
        latitude = entity.latitude,
        longitude = entity.longitude,
        country = Locale("uk", entity.country).displayCountry
    )

    override fun domainToEntityMap(domain: CityName) = CityNameNet(
        name = domain.name,
        latitude = domain.latitude,
        longitude = domain.longitude,
        country = Locale("uk", domain.country).country
    )

    fun entityToDomainMapList(entityList: List<CityNameNet>) =
        entityList.map { entityToDomainMap(it) }

    fun domainToEntityMapList(domainList: List<CityName>): List<CityNameNet> =
        domainList.map { domainToEntityMap(it) }
}