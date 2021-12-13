package edu.uw.tcss450.howlr.model;

import android.app.Application;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import me.pushy.sdk.Pushy;

/**
 * The view model class for pushy tokens for notifications and sending messages.
 */
public class PushyTokenViewModel extends AndroidViewModel {

    /**
     * The pushy token.
     */
    private final MutableLiveData<String> mPushyToken;

    /**
     * The response code.
     */
    private final MutableLiveData<JSONObject> mResponse;

    /**
     * Creates a ViewModel for the pushy token using an application.
     * @param application
     */
    public PushyTokenViewModel(@NonNull Application application) {
        super(application);
        mPushyToken = new MutableLiveData<>();
        mPushyToken.setValue("");
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Adds an observer to the pushy token for notifications and message sending.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addTokenObserver(@NonNull LifecycleOwner owner,
                                 @NonNull Observer<? super String> observer) {
        mPushyToken.observe(owner, observer);
    }

    /**
     * Adds an observer to response code.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Retrieves the pushy token.
     */
    public void retrieveToken() {
        if (!Pushy.isRegistered(getApplication().getApplicationContext())) {

            Log.d("PUSH VIEW MODEL", "FETCHING NEW TOKEN");
            new RegisterForPushNotificationsAsync().execute();

        } else {
            Log.d("PUSH VIEW MODEL", "USING OLD TOKEN");
            mPushyToken.setValue(
                    Pushy.getDeviceCredentials(getApplication().getApplicationContext()).token);
        }
    }

    /**
     * This is the method described in the Pushy documentation. Note the Android class
     * AsyncTask is deprecated as of Android Q. It is fine to use here and for this
     * quarter. In your future Android development, look for an alternative solution.
     */
    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, String> {

        /**
         * Handles the push notifications for registering in the background.
         * @param params The parameters
         * @return The device's token
         */
        protected String doInBackground(Void... params) {
            String deviceToken;
            try {
                // Assign a unique token to this device
                deviceToken = Pushy.register(getApplication().getApplicationContext());
            }
            catch (Exception e) {
                // Return exc to onPostExecute
                return e.getMessage();
            }
            // Success
            return deviceToken;
        }

        /**
         * Sets the pushy token with the new token.
         * @param token The new token
         */
        @Override
        protected void onPostExecute(String token) {
            if (token.isEmpty()) {
                // Show error in log - You should add error handling for the user.
                Log.e("ERROR RETRIEVING PUSHY TOKEN", token);
            } else {
                mPushyToken.setValue(token);
            }
        }
    }

    /**
     * Sends this Pushy device token to the web service.
     * @param jwt The JSON web token
     * @throws IllegalStateException When this method is called before the token is retrieved
     */
    public void sendTokenToWebservice(final String jwt) {
        if (mPushyToken.getValue().isEmpty()) {
            throw new IllegalStateException("No pushy token. Do NOT call until token is retrieved");
        }

        String url = getApplication().getResources().getString(R.string.base_url) +
                "auth";

        JSONObject body = new JSONObject();
        try {
            body.put("token", mPushyToken.getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Handles the error for the HTTP library Volley.
     * @param error The error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Deletes the token from the web service.
     * @param jwt The JSON web token
     */
    public void deleteTokenFromWebservice(final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url) +
                "auth";
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                mResponse::setValue,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
}