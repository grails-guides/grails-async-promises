//tag::packageAndImports[]
package org.openweathermap

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
//end::packageAndImports[]
//tag::promisesImports[]
import static grails.async.Promises.*
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
        RestBuilder rest = new RestBuilder()
        String url = "${openWeatherUrl}/data/2.5/weather?q={city},{countryCode}&appid={appid}"
        Map params = [city: cityName, countryCode: countryCode, appid: appid]
        String unitParam = unitParameter(unit)
        if ( unitParam ) {
            params.units = unitParam
            url += "&units={units}"
        }        
        RestResponse restResponse = rest.get(url) { // <1>
            urlVariables params
        }

        if ( restResponse.statusCode.value() == 200 && restResponse.json ) {
            return OpenweathermapParser.currentWeatherFromJSONElement(restResponse.json) // <2>
        }
        null // <3>
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
    List<CurrentWeather> findCurrentWeatherByCitiesAndCountryCodeWithPromises(List<String> cities, String countryCode, Unit unit) {
        List list = []
        cities.each { String city -> 
            list << task { // <1>
                currentWeather(city, countryCode, unit)
            }
        }
        List l = waitAll(list) // <2>
        l.flatten() as List<CurrentWeather>
    }
//end::findCurrentWeatherByCitiesAndCountryCodeAsync[]
//tag::findCurrentWeatherByCitiesAndCountryCodeSynchronous[]    
    List<CurrentWeather> findCurrentWeatherByCitiesAndCountryCode(List<String> cities, String countryCode, Unit unit) {
        cities.collect { currentWeather(it, countryCode, unit) }    
    }
//end::findCurrentWeatherByCitiesAndCountryCodeSynchronous[]    
}
