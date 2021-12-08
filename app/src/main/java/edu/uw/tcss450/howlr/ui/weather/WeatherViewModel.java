package edu.uw.tcss450.howlr.ui.weather;
import android.app.Application;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implements Weather class to generate current weather, hourly weather, daily weather.
 * @author Edward Robinson, Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<List<Weather>> mWeather;
    private List<String> mLocation;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeather = new MutableLiveData<>();
    }

    public void addWeatherObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Weather>> observer) {
        mWeather.observe(owner, observer);
    }

    public void connectGet(final double lat, final double lon, final String jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
//        String url = "https://howlr-server-side.herokuapp.com/weather/47/-122/";
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
    public void connectZipGet(final int zip, final String jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
//        String url = "https://howlr-server-side.herokuapp.com/weather/47/-122/";
        String url = "https://howlr-server-side.herokuapp.com/weather/zip?" + zip;

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

    private void handleResult(final JSONObject result) {

        if (!result.has("current")) {
            throw new IllegalStateException("Unexpected response in WeatherViewModel: " + result);
        }
        try {
            System.out.println(result);

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


    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }
}