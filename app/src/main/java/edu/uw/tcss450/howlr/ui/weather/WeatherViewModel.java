package edu.uw.tcss450.authentication.ui.weather;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.auth0.android.jwt.JWT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.authentication.MainActivity;
import edu.uw.tcss450.authentication.R;

public class WeatherViewModel extends AndroidViewModel {
    public MutableLiveData<JSONObject> mResponse;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<?super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {
//        mResponse.setValue(result);
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;
            JSONArray a = root.getJSONArray("data");
            JSONObject obj = a.getJSONObject(0);
            mResponse.setValue(obj);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
//        mBlogList.setValue(mBlogList.getValue());
    }

    private void handleError(final VolleyError error) {
        if(Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" + "error:\"" + error.getMessage() + "\"}"));
            } catch(JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"','\'');
            try {
                mResponse.setValue(new JSONObject("{" + "code:" + error.networkResponse.statusCode
                + ", data:\"" + data + "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectGetCurrent() {
        String url = "https://howlr-server-side.herokuapp.com/weather/current";
//        SharedPreferences prefs;
//        prefs = MainActivity.this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
//        String token = prefs.getString();
//        JWT jwt = new JWT(token);
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // headers <key, value>
                headers.put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imdyb3Vwc2NydWZmeUBnbWFpbC5jb20iLCJtZW1iZXJpZCI6ODgsImlhdCI6MTYzNzQ0NDM2NCwiZXhwIjoxNjM4NjUzOTY0fQ.cb5lvS9bboRbhQ_60GdkrdhvEUDF1kie5IAi_HubAdc");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
        Log.d("VOLLEY", "I did the volley thing");
    }
}
