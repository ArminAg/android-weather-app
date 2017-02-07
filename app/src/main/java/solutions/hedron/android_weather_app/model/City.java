package solutions.hedron.android_weather_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by armin on 07/02/2017.
 */

public class City {
    @SerializedName("name")
    public String cityName;
    @SerializedName("country")
    public String country;
}
