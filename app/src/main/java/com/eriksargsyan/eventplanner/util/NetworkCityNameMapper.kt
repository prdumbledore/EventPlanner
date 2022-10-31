package com.eriksargsyan.eventplanner.util

import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.network.CityNameNet
import java.util.*

class NetworkCityNameMapper : EntityToDomainMapper<CityNameNet, CityName> {

    override fun entityToDomainMap(entity: CityNameNet) = CityName(

        name = entity.name,
        latitude = entity.latitude,
        longitude = entity.longitude,
        country = Locale("en", entity.country).displayCountry
    )

    override fun domainToEntityMap(domain: CityName) = CityNameNet(
        name = domain.name,
        latitude = domain.latitude,
        longitude = domain.longitude,
        country = Locale("en", domain.country).country
    )

    override fun entityToDomainMapList(entityList: List<CityNameNet>) =
        entityList.map { entityToDomainMap(it) }

    override fun domainToEntityMapList(domainList: List<CityName>): List<CityNameNet> =
        domainList.map { domainToEntityMap(it) }
}