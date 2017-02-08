package solutions.hedron.android_weather_app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import solutions.hedron.android_weather_app.R;
import solutions.hedron.android_weather_app.holder.WeatherReportViewHolder;
import solutions.hedron.android_weather_app.model.WeatherList;

/**
 * Created by armin on 31/01/2017.
 */

public class WeatherReportAdapter extends RecyclerView.Adapter<WeatherReportViewHolder> {
    private ArrayList<WeatherList> weatherReports;

    public WeatherReportAdapter(ArrayList<WeatherList> weatherReports) {
        this.weatherReports = weatherReports;
    }

    @Override
    public void onBindViewHolder(WeatherReportViewHolder holder, int position) {
        WeatherList report = weatherReports.get(position);
        holder.updateUI(report);
    }

    @Override
    public WeatherReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather,parent, false);
        return new WeatherReportViewHolder(card);
    }

    @Override
    public int getItemCount() {
        return weatherReports.size();
    }
}
