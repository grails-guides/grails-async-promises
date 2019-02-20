//tag::packageAndImports[]
package org.openweathermap

import grails.async.Promise
import grails.async.PromiseList
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import org.grails.web.json.JSONObject
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

//end::packageAndImports[]
//tag::promisesImports[]
import static grails.async.Promises.*
//end::promisesImports[]

//tag::grailsConfigurationAware[]
@CompileStatic
class OpenweathermapService {

    @Autowired OpenweathermapConfiguration openweathermapConfiguration
//end::grailsConfigurationAware[]

//tag::currentWeather[]
    @CompileDynamic
    CurrentWeather currentWeather(Unit units = Unit.Standard) {
        currentWeather(openweathermapConfiguration.cityName, openweathermapConfiguration.countryCode, units)
    }


    @CompileDynamic
    CurrentWeather currentWeather(String cityName, String countryCode, Unit unit = Unit.Standard) {
        HttpClient client = HttpClient.create(openweathermapConfiguration.openWeatherUrl.toURL())
        String uri = "/data/2.5/weather?q=${cityName},${countryCode}&appid=${openweathermapConfiguration.appid}"
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

//tag::findCurrentWeatherByCitiesAndCountryCodeAsync[]
    Promise<List<CurrentWeather>> findCurrentWeatherByCitiesAndCountryCodeWithPromises(List<String> cities, String countryCode, Unit unit) {
        PromiseList<CurrentWeather> list = new PromiseList<CurrentWeather>()
        cities.each { String city -> 
            list << task { // <1>
                currentWeather(city, countryCode, unit)
            }
        }
        return list // <2>
    }
//end::findCurrentWeatherByCitiesAndCountryCodeAsync[]
//tag::findCurrentWeatherByCitiesAndCountryCodeSynchronous[]    
    List<CurrentWeather> findCurrentWeatherByCitiesAndCountryCode(List<String> cities, String countryCode, Unit unit) {
        cities.collect { currentWeather(it, countryCode, unit) }    
    }
//end::findCurrentWeatherByCitiesAndCountryCodeSynchronous[]    
}
