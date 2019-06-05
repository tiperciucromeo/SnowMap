package com.example.snowmap;

public class WeatherInfo {
    private double minTemperature;
    private double maxTemperature;
    private double humidity;
    private double speedWind;
    private String description;
    //TODO: snow

    public WeatherInfo(double minTemperature, double maxTemperature, double humidity, double speedWind, String description) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.humidity = humidity;
        this.speedWind = speedWind;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getSpeedWind() {
        return speedWind;
    }

    public void setSpeedWind(double speedWind) {
        this.speedWind = speedWind;
    }
}
