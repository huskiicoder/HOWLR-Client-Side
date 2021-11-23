package edu.uw.tcss450.howlr.ui.messages;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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
import java.util.function.IntFunction;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.ui.messages.chats.ChatMessage;

public class MessagesListViewModel extends AndroidViewModel  {

    private MutableLiveData<List<ChatMessage>> mMessagesList;

    public MessagesListViewModel(@NonNull Application application) {
        super(application);
        mMessagesList = new MutableLiveData<>();
        mMessagesList.setValue(new ArrayList<>());
    }

    public void addBlogListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<ChatMessage>> observer) {
        mMessagesList.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        // you should add much better error handling in a production release.
        // i.e. YOUR PROJECT
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
      /*  try {
            JSONObject root = result;
            if (root.has(getString.apply(R.string.keys_json_blogs_response))) {
                JSONObject response =
                        root.getJSONObject(getString.apply(
                                R.string.keys_json_blogs_response));
                if (response.has(getString.apply(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString.apply(R.string.keys_json_blogs_data));

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        ChatMessage post = new ChatMessage.Builder(
                                jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString.apply(
                                                R.string.keys_json_blogs_url)))
                                .build();
                        if (!mMessagesList.getValue().contains(post)) {
                            mMessagesList.getValue().add(post);
                        }
                    }
                } else {
                    Log.e("ERROR!", "No data array");
                }
            } else {
                Log.e("ERROR!", "No response");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        } */

        mMessagesList.setValue(mMessagesList.getValue());
    }


    public void connectGet() {
        String url = "https://howlr-server-side.herokuapp.com/messages/get";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                //no body for this get request
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                                "eyJlbWFpbCI6InV3bmV0aWQyQGZha2UuZW1haWwuY29tI" +
                                "iwibWVtYmVyaWQiOjEyMTcsImlhdCI6MTYzNTIwMzI0MSwi" +
                                "ZXhwIjoxNjQzODQzMjQxfQ.BA02IgpIT8xLWQ_-b1Su909vfie3si2PpU5B-8sxVZk");
                return headers;
            }
        };
    }

}
