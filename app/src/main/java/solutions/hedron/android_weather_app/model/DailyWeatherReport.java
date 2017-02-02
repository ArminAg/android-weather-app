package solutions.hedron.android_weather_app.model;

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

    public DailyWeatherReport(int currentTemp, int maxTemp, int minTemp, String rawDate, String weatherType) {
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.formattedDate = formatRawDate(rawDate);
        this.weatherType = weatherType;
    }

    protected String formatRawDate(String rawDate){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = "";
        try {
            Date date = sourceFormat.parse(rawDate);
            SimpleDateFormat destinationFormat = new SimpleDateFormat("EEE, MMM d");
            result = destinationFormat.format(date);
        } catch (ParseException ex) {

        }
        return result;
    }
}
