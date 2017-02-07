package solutions.hedron.android_weather_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by armin on 07/02/2017.
 */

public class WeatherList{
    @SerializedName("dt_txt")
    public Date date;

    public Main main;

    public List<Weather> weather;
}
