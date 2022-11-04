package com.eriksargsyan.eventplanner.util.mappers

interface EntityToDomainMapper<Entity, Domain> {

    fun entityToDomainMap(entity: Entity): Domain

    fun domainToEntityMap(domain: Domain): Entity


}