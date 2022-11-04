package com.eriksargsyan.eventplanner.util.mappers

import com.eriksargsyan.eventplanner.data.model.domain.Weather
import com.eriksargsyan.eventplanner.data.model.network.WeatherDetailsNet
import com.eriksargsyan.eventplanner.data.model.network.WeatherListNet
import com.eriksargsyan.eventplanner.data.model.network.WeatherNet
import com.eriksargsyan.eventplanner.data.model.network.WeatherTempNet
import kotlin.math.round


class NetworkWeatherMapper : EntityToDomainMapper<WeatherListNet, List<Weather>> {

    override fun entityToDomainMap(entity: WeatherListNet): List<Weather> {
        return entity.weatherItem.map {
            Weather(
                weatherTemp = round(it.weatherTemp.temp).toInt().toString(),
                weatherIcon = it.weatherIcon[0].iconId.dropLast(1),
                date = it.date,
            )
        }
    }

    override fun domainToEntityMap(domain: List<Weather>): WeatherListNet {
        return WeatherListNet(
            domain.map {
                WeatherNet(
                    weatherTemp = WeatherTempNet(it.weatherTemp.toDouble()),
                    weatherIcon = listOf(WeatherDetailsNet(it.weatherIcon)),
                    date = it.date,
                )
            }

        )
    }
}
