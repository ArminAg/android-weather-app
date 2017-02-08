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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.ArrayList;

import solutions.hedron.android_weather_app.adapter.WeatherReportAdapter;
import solutions.hedron.android_weather_app.model.Weather;
import solutions.hedron.android_weather_app.model.WeatherList;
import solutions.hedron.android_weather_app.model.WeatherModel;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_LOCATION = 111; // Some Arbitrary number
    private static MainActivity mainActivity;

    final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "/?lat=";
    final String URL_UNITS = "&units=metric";
    final String URL_API_KEY = "&APPID=60b31c8d0b09373fc658ed16b577cffa";

    private TextView todayDate;
    private TextView currentTemp;
    private TextView lowTemp;
    private ImageView weatherIcon;
    private TextView cityCountry;
    private TextView weatherDescription;

    private ArrayList<WeatherList> weatherList = new ArrayList<>();
    private WeatherModel weatherModel;
    private WeatherReportAdapter adapter;

    private RequestQueue requestQueue;
    private Gson gson;

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

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        gson = gsonBuilder.create();

        this.todayDate = (TextView)findViewById(R.id.todayDate);
        this.currentTemp = (TextView)findViewById(R.id.currentTemp);
        this.lowTemp = (TextView)findViewById(R.id.lowTemp);
        this.weatherIcon = (ImageView)findViewById(R.id.weatherIcon);
        this.cityCountry = (TextView)findViewById(R.id.cityCountry);
        this.weatherDescription = (TextView)findViewById(R.id.weatherDescription);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.content_daily_weather);
        adapter = new WeatherReportAdapter(weatherList);
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
        WeatherList weatherItem = weatherModel.list.get(0);
        todayDate.setText(weatherItem.getFormattedDate());
        currentTemp.setText(Integer.toString(weatherItem.main.temp.intValue()) + "°");
        lowTemp.setText(Integer.toString(weatherItem.main.minTemp.intValue()) + "°");
        cityCountry.setText(weatherModel.city.cityName + ", " + weatherModel.city.country);
        weatherDescription.setText(weatherItem.weather.get(0).weatherType);

        switch (weatherItem.weather.get(0).weatherType){
            case Weather.WEATHER_TYPE_SUN:
                this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.sunny));
                break;
            case Weather.WEATHER_TYPE_CLOUDS:
                this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.cloudy));
                break;
            case Weather.WEATHER_TYPE_PARTIAL_CLOUDS:
                this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.partially_cloudy));
                break;
            case Weather.WEATHER_TYPE_RAIN:
                this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.rainy));
                break;
            case Weather.WEATHER_TYPE_SNOW:
                this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.snow));
                break;
            case Weather.WEATHER_TYPE_THUNDER:
                this.weatherIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.getMainActivity().getApplicationContext(), R.drawable.thunder_lightning));
                break;
        }
    }


    public void downloadWeatherData(Location location){
        final String fullcoords = URL_COORD + location.getLatitude() + "&lon=" + location.getLongitude();
        final String url = URL_BASE + fullcoords + URL_UNITS + URL_API_KEY;

        StringRequest request = new StringRequest(Request.Method.GET, url, onWeatherReportLoaded, onWeatherReportError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onWeatherReportLoaded = new Response.Listener<String>(){
        @Override
        public void onResponse(String response){
            weatherModel = gson.fromJson(response, WeatherModel.class);
            DateTime currentDateTime = null;
            for (WeatherList item : weatherModel.list){
                DateTime tempDate = new DateTime(item.date);

                if (currentDateTime == null){
                    currentDateTime = tempDate;
                }

                if (tempDate.toLocalDate().isAfter(currentDateTime.toLocalDate()))
                {
                    currentDateTime = tempDate;
                    weatherList.add(item);
                }
            }
            updateUI();
            adapter.notifyDataSetChanged();
        }
    };

    private final Response.ErrorListener onWeatherReportError = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            Log.e("VOLLEY_ERROR", error.toString());
        }
    };

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
