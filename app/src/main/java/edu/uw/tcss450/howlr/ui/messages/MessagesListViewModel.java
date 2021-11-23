package edu.uw.tcss450.howlr.ui.messages;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;

public class MessagesListViewModel extends AndroidViewModel  {

    public MutableLiveData<List<MessageModel>> mMessagesList;

    public MessagesListViewModel(@NonNull Application application) {
        super(application);
        mMessagesList = new MutableLiveData<>();
        mMessagesList.setValue(new ArrayList<>());
    }

    private void handleError(final VolleyError error) {
        System.out.println("in error");
        // you should add much better error handling in a production release.
        // i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
        System.out.println("in result");
        try {
            JSONArray messages = result.getJSONArray("rows");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject jsonMessage = messages.getJSONObject(i);

                MessageModel cm = new MessageModel(
                        R.drawable.shibabone,
                        jsonMessage.getInt("chatid"),
                        jsonMessage.getString("firstname"),
                        jsonMessage.getString("message"),
                        jsonMessage.getString("timestamp"));
                if (!mMessagesList.getValue().contains(cm)) {
                    mMessagesList.getValue().add(cm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

        System.out.println("size: " + this.mMessagesList.getValue().size());
        mMessagesList.setValue(mMessagesList.getValue());
        System.out.println("size after: " + this.mMessagesList.getValue().size());
    }


    public void connectGet(final String jwt) {
        String url = "https://howlr-server-side.herokuapp.com/chats/88";
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

/*    public MutableLiveData<List<MessageModel>> getMessagesList() {
        return mMessagesList;
    }*/


}
