package solutions.hedron.android_weather_app.holder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import solutions.hedron.android_weather_app.MainActivity;
import solutions.hedron.android_weather_app.R;
import solutions.hedron.android_weather_app.model.Weather;
import solutions.hedron.android_weather_app.model.WeatherList;

/**
 * Created by armin on 31/01/2017.
 */

public class WeatherReportViewHolder extends RecyclerView.ViewHolder {
    private ImageView cardWeatherIcon;
    private TextView cardWeatherDay;
    private TextView cardWeatherDescription;
    private TextView cardTempHigh;
    private TextView cardTempLow;

    public WeatherReportViewHolder(View itemView) {
        super(itemView);

        this.cardWeatherIcon = (ImageView)itemView.findViewById(R.id.cardWeatherIcon);
        this.cardWeatherDay = (TextView)itemView.findViewById(R.id.cardWeatherDay);
        this.cardWeatherDescription = (TextView)itemView.findViewById(R.id.cardWeatherDescription);
        this.cardTempHigh = (TextView)itemView.findViewById(R.id.cardTempHigh);
        this.cardTempLow = (TextView)itemView.findViewById(R.id.cardTempLow);
    }

    public void updateUI(WeatherList report){
        this.cardWeatherDay.setText(report.getFormattedDate());
        this.cardWeatherDescription.setText(report.weather.get(0).weatherType);
        this.cardTempHigh.setText(Integer.toString(report.main.maxTemp.intValue()) + "°");
        this.cardTempLow.setText(Integer.toString(report.main.minTemp.intValue()) + "°");

        switch (report.weather.get(0).weatherType){
            case Weather.WEATHER_TYPE_SUN:
                this.cardWeatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.sunny_mini));
                break;
            case Weather.WEATHER_TYPE_CLOUDS:
                this.cardWeatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.cloudy_mini));
                break;
            case Weather.WEATHER_TYPE_PARTIAL_CLOUDS:
                this.cardWeatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.partially_cloudy));
                break;
            case Weather.WEATHER_TYPE_RAIN:
                this.cardWeatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.rainy_mini));
                break;
            case Weather.WEATHER_TYPE_SNOW:
                this.cardWeatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.snow_mini));
                break;
            case Weather.WEATHER_TYPE_THUNDER:
                this.cardWeatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.thunder_lightning_mini));
                break;
        }
    }
}
