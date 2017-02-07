package solutions.hedron.android_weather_app.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by armin on 31/01/2017.
 */

public class DailyWeatherReport {

    public static final String WEATHER_TYPE_CLOUDS = "Clouds";
    public static final String WEATHER_TYPE_PARTIAL_CLOUDS = "Partially Cloudy";
    public static final String WEATHER_TYPE_SUN = "Clear";
    public static final String WEATHER_TYPE_RAIN = "Rain";
    public static final String WEATHER_TYPE_SNOW = "Snow";
    public static final String WEATHER_TYPE_THUNDER = "Thunderstorm";

    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private String formattedDate;
    private String weatherType;

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getWeather() {
        return weatherType;
    }

    public DailyWeatherReport(int currentTemp, int maxTemp, int minTemp, Date rawDate, String weatherType) {
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.formattedDate = formatRawDate(rawDate);
        this.weatherType = weatherType;
    }

    protected String formatRawDate(Date rawDate){
        DateTime formattedDate = new DateTime(rawDate);
        return formattedDate.dayOfWeek().getAsText() + ", " + formattedDate.monthOfYear().getAsShortText() + " " + formattedDate.dayOfMonth().get();
    }
}
