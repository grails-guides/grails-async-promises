package demo

import groovy.transform.CompileStatic
import org.openweathermap.CurrentWeather
import org.openweathermap.OpenweathermapService
import org.openweathermap.Unit

@CompileStatic
class CitiesController {
    
    OpenweathermapService openweathermapService
    
    def index(String unit, boolean async) {
        Unit unitEnum = Unit.unitWithString(unit)
        List<CurrentWeather> currentWeatherList
        if ( async ) {
        	currentWeatherList = openweathermapService.findCurrentWeatherByCitiesAndCountryCodeWithPromises(LargestUSCities.CITIES, 'us', unitEnum)
        } else {
        	currentWeatherList = openweathermapService.findCurrentWeatherByCitiesAndCountryCode(LargestUSCities.CITIES, 'us', unitEnum)
        }        
        [currentWeatherList: currentWeatherList, unit: unitEnum]
    }
}