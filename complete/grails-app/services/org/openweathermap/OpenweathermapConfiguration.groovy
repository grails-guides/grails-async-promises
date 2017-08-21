package org.openweathermap

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Configuration for {@link OpenweathermapService}
 */
@ConfigurationProperties(prefix = 'openweather')
@Component
class OpenweathermapConfiguration {

    String appid
    String cityName
    String countryCode
    String openWeatherUrl = 'http://api.openweathermap.org'
}
