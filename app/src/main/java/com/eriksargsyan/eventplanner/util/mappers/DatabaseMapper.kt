package com.eriksargsyan.eventplanner.util.mappers

import com.eriksargsyan.eventplanner.data.model.database.EventDB
import com.eriksargsyan.eventplanner.data.model.domain.Event

class DatabaseMapper : EntityToDomainMapper<EventDB, Event> {

    override fun entityToDomainMap(entity: EventDB) = Event(
        id = entity.id,
        eventName = entity.eventName,
        date = entity.date,
        cityName = entity.cityName,
        latitude = entity.latitude,
        longitude = entity.longitude,
        addressLine = entity.addressLine,
        description = entity.description,
        country = entity.country,
        status = entity.status,
        weather = entity.weather,
    )

    override fun domainToEntityMap(domain: Event) = EventDB (
        id = domain.id,
        eventName = domain.eventName,
        date = domain.date,
        cityName = domain.cityName,
        latitude = domain.latitude,
        longitude = domain.longitude,
        addressLine = domain.addressLine,
        description = domain.description,
        country = domain.country,
        status = domain.status,
        weather = domain.weather,
    )

    fun entityToDomainMapList(entityList: List<EventDB>) =
        entityList.map { entityToDomainMap(it) }

    fun domainToEntityMapList(domainList: List<Event>) =
        domainList.map { domainToEntityMap(it) }
}