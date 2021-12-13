package edu.uw.tcss450.howlr.ui.weather;
import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * The ViewModel for the register fragment for the weather page (daily and hourly weather).
 * @author Edward Robinson, Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherViewModel extends AndroidViewModel {

    /**
     * The list of weather objects for the live data.
     */
    private MutableLiveData<List<Weather>> mWeather;

    /**
     * The list of weather value mappings for the location data.
     */
    private MutableLiveData<Map<String, Double>> mLocationData;

    /**
     * The ViewModel for the user's information.
     */
    private UserInfoViewModel mUserModel;

    /**
     * Creates the ViewModel for the weather fragment given an application.
     * @param application The application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeather = new MutableLiveData<>();
        mLocationData = new MutableLiveData<>();
    }

    /**
     * Adds an observer to the weather fragment for the weather.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addWeatherObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Weather>> observer) {
        mWeather.observe(owner, observer);
    }

    /**
     * Adds an observer to the weather fragment for the location.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addLocationObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super Map<String, Double>> observer) {
        mLocationData.observe(owner, observer);
    }

    /**
     * Connects to the web server to get the weather.
     * @param lat The latitude coordinates
     * @param lon The longitude coordinates
     * @param jwt The JSON web token
     */
    public void connectGet(final String lat, final String lon, final String jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/weather?lat=" + lat + "&lon=" + lon;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Connects to the web server with the zipcode to get the weather.
     * @param zip The zipcode
     * @param jwt The JSON web token
     */
    public void connectZipGet(final String zip, final String jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/weather?zip=" + zip;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Handles the result the web server returns.
     * @param result The result
     */
    private void handleResult(final JSONObject result) {
        if (!result.has("current")) {
            throw new IllegalStateException("Unexpected response in WeatherViewModel: " + result);
        }
        try {
            Map<String, Double> location = new HashMap<>();
            Double lat = result.getDouble("lat");
            Double lon = result.getDouble("lon");

            location.put("lat", lat);
            location.put("lon", lon);

            mLocationData.setValue(location);

            JSONObject currentData = result.getJSONObject("current");

            JSONArray hourlyArray = result.getJSONArray("hourly");

            JSONArray dailyArray = result.getJSONArray("daily");
            ArrayList<Weather> weatherList = new ArrayList<Weather>();
            Calendar currentTime = Calendar.getInstance();
            String[] days = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

            weatherList.add(new Weather(currentData.getDouble("temp"),
                    currentData.getJSONArray("weather").getJSONObject(0).getString("main"),
                    days[currentTime.get(Calendar.DAY_OF_WEEK)],
                    "Tacoma",
                    currentData.getJSONArray("weather").getJSONObject(0).getString("icon"),
                    currentData.getInt("humidity")));

            for (int i = 0; i < 24; i++) {
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                Weather someHour = new Weather(
                        hour, hourlyArray.getJSONObject(i).getDouble("temp"),
                        hourlyArray.getJSONObject(i).getJSONArray("weather")
                                .getJSONObject(0).getString("main"),
                        hourlyArray.getJSONObject(i).getJSONArray("weather")
                                .getJSONObject(0).getString("icon"),
                        hourlyArray.getJSONObject(i).getInt("humidity"));
                weatherList.add(someHour);
                currentTime.add(Calendar.HOUR, 1);
            }

            currentTime = Calendar.getInstance();
            for (int i = 0; i < 8; i++) {
                Weather someDay = new Weather(
                        dailyArray.getJSONObject(i).getJSONObject("temp").getDouble("max"),
                        dailyArray.getJSONObject(i).getJSONObject("temp").getDouble("min"),
                        days[currentTime.get(Calendar.DAY_OF_WEEK)],
                        dailyArray.getJSONObject(i).getJSONArray("weather")
                                .getJSONObject(0).getString("icon"),
                        dailyArray.getJSONObject(i).getInt("humidity"));
                weatherList.add(someDay);
                currentTime.add(Calendar.DAY_OF_WEEK, 1);
            }

            mWeather.setValue(weatherList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the error for the HTTP library Volley.
     * @param error The error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", "WEATHERVIEWMODEL");
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    /**
     * Sets the ViewModel for the user's information.
     * @param userInfoViewModel The ViewModel
     */
    public void setUserInfoViewModel(UserInfoViewModel userInfoViewModel) {
        mUserModel = userInfoViewModel;
    }
}