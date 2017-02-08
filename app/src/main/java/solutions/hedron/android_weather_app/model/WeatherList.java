package solutions.hedron.android_weather_app.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

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

    public String getFormattedDate(){
        DateTime formattedDate = new DateTime(this.date);
        return formattedDate.dayOfWeek().getAsText() + ", " + formattedDate.monthOfYear().getAsShortText() + " " + formattedDate.dayOfMonth().get();
    }
}
