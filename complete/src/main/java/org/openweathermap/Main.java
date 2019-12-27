//tag::clazz[]
package org.openweathermap;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class Main {

    @JsonProperty("temp")
    private BigDecimal temperature;

    private BigDecimal pressure;

    private Integer humidity;

    @JsonProperty("temp_min")
    private BigDecimal tempMin;

    @JsonProperty("temp_max")
    private BigDecimal tempMax;

    private BigDecimal seaLevel;

    private BigDecimal groundLevel;

    public Main() {}

    //end::clazz[]
    //tag::gettersandsetters[]
    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getTempMin() {
        return tempMin;
    }

    public void setTempMin(BigDecimal tempMin) {
        this.tempMin = tempMin;
    }

    public BigDecimal getTempMax() {
        return tempMax;
    }

    public void setTempMax(BigDecimal tempMax) {
        this.tempMax = tempMax;
    }

    public BigDecimal getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(BigDecimal seaLevel) {
        this.seaLevel = seaLevel;
    }

    public BigDecimal getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(BigDecimal groundLevel) {
        this.groundLevel = groundLevel;
    }
    //end::gettersandsetters[]
}
