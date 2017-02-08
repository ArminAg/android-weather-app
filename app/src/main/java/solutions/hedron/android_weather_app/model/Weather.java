package solutions.hedron.android_weather_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by armin on 07/02/2017.
 */

public class Weather {
    public static final String WEATHER_TYPE_CLOUDS = "Clouds";
    public static final String WEATHER_TYPE_PARTIAL_CLOUDS = "Partially Cloudy";
    public static final String WEATHER_TYPE_SUN = "Clear";
    public static final String WEATHER_TYPE_RAIN = "Rain";
    public static final String WEATHER_TYPE_SNOW = "Snow";
    public static final String WEATHER_TYPE_THUNDER = "Thunderstorm";

    @SerializedName("main")
    public String weatherType;
}
