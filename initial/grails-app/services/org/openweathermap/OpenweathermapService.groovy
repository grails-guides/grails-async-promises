//tag::packageAndImports[]
package org.openweathermap

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import org.grails.web.json.JSONObject

//end::packageAndImports[]
//tag::promisesImports[]

//end::promisesImports[]

//tag::grailsConfigurationAware[]
@CompileStatic
class OpenweathermapService implements GrailsConfigurationAware {
    String appid
    String cityName
    String countryCode
    String openWeatherUrl

    @Override
    void setConfiguration(Config co) {
        openWeatherUrl = co.getProperty('openweather.url', String, 'http://api.openweathermap.org')
        appid = co.getProperty('openweather.appid', String)
        cityName = co.getProperty('openweather.cityName', String)
        countryCode = co.getProperty('openweather.countryCode', String)
    }
//end::grailsConfigurationAware[]

//tag::currentWeather[]
    @CompileDynamic
    CurrentWeather currentWeather(Unit units = Unit.Standard) {
        currentWeather(cityName, countryCode, units)
    }


    @CompileDynamic
    CurrentWeather currentWeather(String cityName, String countryCode, Unit unit = Unit.Standard) {
        HttpClient client = HttpClient.create(openWeatherUrl.toURL())
        String uri = "/data/2.5/weather?q=${cityName},${countryCode}&appid=${appid}"
        String unitParam = unitParameter(unit)
        if ( unitParam ) {
            uri += "&units=${unitParam}"
        }
        try {
            HttpResponse<Map> resp = client.toBlocking().exchange(HttpRequest.GET(uri), Map)
            if ( resp.status == HttpStatus.OK && resp.body() ) {
                return OpenweathermapParser.currentWeatherFromJSONElement(new JSONObject(resp.body())) // <2>
            }
        } catch (Exception e) {
            return null // <3>
        }
    }

  /**
    * @return null if Standard Unit
    */
    String unitParameter(Unit unit)  {
        switch ( unit ) {
            case Unit.Metric:
                return 'metric'
            case Unit.Imperial:
                return 'imperial'                
            default:
                return null
        }
    }
//end::currentWeather[]
}
