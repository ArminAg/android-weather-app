package solutions.hedron.android_weather_app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by armin on 07/02/2017.
 */

public class Main {
    public Double temp;
    @SerializedName("temp_min")
    public Double minTemp;
    @SerializedName("temp_max")
    public Double maxTemp;
}
