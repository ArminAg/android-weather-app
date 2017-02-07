package solutions.hedron.android_weather_app.model;

import java.util.Date;

/**
 * Created by armin on 02/02/2017.
 */

public class WeatherReportHeader extends DailyWeatherReport {
    private String cityName;
    private String country;

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public WeatherReportHeader(String cityName, String country, int currentTemp, int maxTemp, int minTemp, Date rawDate, String weatherType) {
        super(currentTemp, maxTemp, minTemp, rawDate, weatherType);
        this.cityName = cityName;
        this.country = country;
    }
}
