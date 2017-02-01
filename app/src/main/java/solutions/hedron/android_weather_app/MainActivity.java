package solutions.hedron.android_weather_app;

import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import solutions.hedron.android_weather_app.adapter.WeatherReportAdapter;
import solutions.hedron.android_weather_app.model.DailyWeatherReport;
import solutions.hedron.android_weather_app.service.DataService;
import solutions.hedron.android_weather_app.service.WeatherApi;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_LOCATION = 111; // Some Arbitrary number
    private static MainActivity mainActivity;

    final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "/?lat=";//9.991354&lon=76.287129";
    final String URL_UNITS = "&units=metric";
    final String URL_API_KEY = "&APPID=60b31c8d0b09373fc658ed16b577cffa";

    private TextView todayDate;
    private TextView currentTemp;
    private TextView lowTemp;
    private ImageView weatherIcon;
    private TextView cityCountry;
    private TextView weatherDescription;

    private ArrayList<DailyWeatherReport> weatherReportList = new ArrayList<>();
    private WeatherReportAdapter adapter;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.setMainActivity(this);

        this.todayDate = (TextView)findViewById(R.id.todayDate);
        this.currentTemp = (TextView)findViewById(R.id.currentTemp);
        this.lowTemp = (TextView)findViewById(R.id.lowTemp);
        this.weatherIcon = (ImageView)findViewById(R.id.weatherIcon);
        this.cityCountry = (TextView)findViewById(R.id.cityCountry);
        this.weatherDescription = (TextView)findViewById(R.id.weatherDescription);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.content_daily_weather);
        adapter = new WeatherReportAdapter(weatherReportList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void updateUI(){
        if (weatherReportList.size() > 0 ){
            DailyWeatherReport report = weatherReportList.get(0);
            todayDate.setText("Today, Jan 1");
            currentTemp.setText(Integer.toString(report.getCurrentTemp()) + "°");
            lowTemp.setText(Integer.toString(report.getMinTemp()) + "°");
            cityCountry.setText(report.getCityName() + ", " + report.getCountry());
            weatherDescription.setText(report.getWeather());

            switch (report.getWeather()){
                case DailyWeatherReport.WEATHER_TYPE_SUN:
                    this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.sunny));
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.cloudy));
                case DailyWeatherReport.WEATHER_TYPE_PARTIAL_CLOUDS:
                    this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.partially_cloudy));
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.rainy));
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.snow));
                case DailyWeatherReport.WEATHER_TYPE_THUNDER:
                    this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.thunder_lightning));
            }
        }
    }

    public void downloadWeatherData(Location location){
        final String fullcoords = URL_COORD + location.getLatitude() + "&lon=" + location.getLongitude();
        final String url = URL_BASE + fullcoords + URL_UNITS + URL_API_KEY;

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject city = response.getJSONObject("city");
                            String cityName = city.getString("name");
                            String country = city.getString("country");

                            JSONArray list = response.getJSONArray("list");

                            for (int x = 0; x < 5; x++){
                                JSONObject obj = list.getJSONObject(x);
                                JSONObject main = obj.getJSONObject("main");
                                Double currentTemp = main.getDouble("temp");
                                Double maxTemp = main.getDouble("temp_max");
                                Double minTemp = main.getDouble("temp_min");

                                JSONArray weatherArr = obj.getJSONArray("weather");
                                JSONObject weather = weatherArr.getJSONObject(0);
                                String weatherType = weather.getString("main");

                                String rawDate = obj.getString("dt_txt");

                                DailyWeatherReport report = new DailyWeatherReport(cityName, country, currentTemp.intValue(),maxTemp.intValue(), minTemp.intValue(), weatherType, rawDate);
                                weatherReportList.add(report);
                            }
                        } catch (JSONException exception){

                        }
                        updateUI();
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", "Error: " + error.getLocalizedMessage());
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    // Google Api Methods
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        } else {
            startLocationServices();
        }
    }

    private void startLocationServices(){
        try {
            LocationRequest request = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
        } catch (SecurityException ex){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationServices();
                } else {
                    Toast.makeText(this, "Location can't be used if permission is denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        downloadWeatherData(location);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
