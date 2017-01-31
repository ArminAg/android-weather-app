package solutions.hedron.android_weather_app.model;

/**
 * Created by armin on 31/01/2017.
 */

public class DailyWeatherReport {

    public static final String WEATHER_TYPE_CLOUDS = "Cloudy";
    public static final String WEATHER_TYPE_PARTIAL_CLOUDS = "Partially Cloudy";
    public static final String WEATHER_TYPE_SUN = "Sunny";
    public static final String WEATHER_TYPE_RAIN = "Rainy";
    public static final String WEATHER_TYPE_SNOW = "Snow";
    public static final String WEATHER_TYPE_THUNDER = "Thunder";

    private String cityName;
    private String country;
    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private String formattedDate;
    private String weather;


    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

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
        return weather;
    }

    public DailyWeatherReport(String cityName, String country, int currentTemp, int maxTemp, int minTemp, String rawDate, String weather) {
        this.cityName = cityName;
        this.country = country;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.formattedDate = formatRawDate(rawDate);
        this.weather = weather;
    }

    private String formatRawDate(String rawDate){
        return "May 1";
    }
}
