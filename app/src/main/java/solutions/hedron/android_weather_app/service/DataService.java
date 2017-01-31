package solutions.hedron.android_weather_app.service;

import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solutions.hedron.android_weather_app.MainActivity;
import solutions.hedron.android_weather_app.model.DailyWeatherReport;

/**
 * Created by armin on 31/01/2017.
 */
public class DataService {
    final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "/?lat=";
    final String URL_UNITS = "&units=metric";
    final String URL_API_KEY = "&APPID=60b31c8d0b09373fc658ed16b577cffa";

    private static DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {
    }

    public ArrayList<DailyWeatherReport> getWeatherReport(Location location){
        final String fullCoordinates = URL_COORD + location.getLatitude() + "&lon=" + location.getLongitude();
        final String url = URL_BASE + fullCoordinates + URL_UNITS + URL_API_KEY;

        final ArrayList<DailyWeatherReport> weatherReportList = new ArrayList<>();
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject city = response.getJSONObject("city");
                            String cityName = city.getString("name");
                            String country = city.getString("country");

                            JSONArray list = response.getJSONArray("list");

                            for (int x = 0; x < 5; x++) {
                                JSONObject obj = list.getJSONObject(x);
                                JSONObject main = obj.getJSONObject("main");
                                Double currentTemp = main.getDouble("temp");
                                Double maxTemp = main.getDouble("temp_max");
                                Double minTemp = main.getDouble("temp_min");

                                JSONArray weatherArray = obj.getJSONArray("weather");
                                JSONObject weather = weatherArray.getJSONObject(0);
                                String weatherType = weather.getString("main");
                                String rawDate = obj.getString("dt_txt");

                                DailyWeatherReport report = new DailyWeatherReport(cityName, country, currentTemp.intValue(), maxTemp.intValue(), minTemp.intValue(), rawDate, weatherType);
                                weatherReportList.add(report);
                            }
                        } catch (JSONException ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("WEATHER_APP", "Error: " + error.getLocalizedMessage());
            }
        });

        Volley.newRequestQueue(MainActivity.getMainActivity()).add(jsonRequest);
        return weatherReportList;
    }
}
