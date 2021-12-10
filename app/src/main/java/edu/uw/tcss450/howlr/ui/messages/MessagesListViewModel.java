package edu.uw.tcss450.howlr.ui.messages;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

public class MessagesListViewModel extends AndroidViewModel  {

    public MutableLiveData<List<MessageModel>> mMessagesList;

    public MessagesListViewModel(@NonNull Application application) {
        super(application);
        mMessagesList = new MutableLiveData<>();
        mMessagesList.setValue(new ArrayList<>());
    }

    private void handleError(final VolleyError error) {
        // you should add much better error handling in a production release.
        // i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
        try {
            JSONArray messages = result.getJSONArray("rows");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject jsonMessage = messages.getJSONObject(i);

                // TODO------------------------------------------------------------------------------------
                MessageModel cm = new MessageModel(
                        R.drawable.shibabone,
                        jsonMessage.getInt("chatid"),
                        jsonMessage.getString("firstname") +
                        " " + jsonMessage.getString("lastname"),
                        jsonMessage.getString("timestamp"),
                        jsonMessage.getString("message"));
                if (!mMessagesList.getValue().contains(cm)) {
                    mMessagesList.getValue().add(cm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mMessagesList.setValue(mMessagesList.getValue());
    }


    public void connectGet(final String jwt, final int memberId) {
        // TODO------------------------------------------------------------------------------------
        String url = "https://howlr-server-side.herokuapp.com/chats/" + memberId;

//        String url = "https://howlr-server-side.herokuapp.com/chats/" + memberId;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
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
