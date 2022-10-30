package com.eriksargsyan.eventplanner.util

interface EntityToDomainMapper<Entity, Domain> {

    fun entityToDomainMap(entity: Entity): Domain

    fun domainToEntityMap(domain: Domain): Entity

    fun entityToDomainMapList(entityList: List<Entity>): List<Domain>

    fun domainToEntityMapList(domainList: List<Domain>): List<Entity>
}