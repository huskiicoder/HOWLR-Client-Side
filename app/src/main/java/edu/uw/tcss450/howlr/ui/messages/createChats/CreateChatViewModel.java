package edu.uw.tcss450.howlr.ui.messages.createChats;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.friends.Friends;

public class CreateChatViewModel extends AndroidViewModel {

    private MutableLiveData<List<Friends>> mFriendsList;

    private final MutableLiveData<JSONObject> mResponse;

    private UserInfoViewModel mUserModel;

    public CreateChatViewModel(@NonNull Application application) {
        super(application);
        mFriendsList = new MutableLiveData<>();
        mFriendsList.setValue(new ArrayList<>());

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public  void addFriendObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<Friends>> observer) {
        mFriendsList.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    private void handleResult(final JSONObject result) {
        try {
            JSONObject root = result;

            JSONArray friends = root.getJSONArray("contact");
            ArrayList<Friends> listOfFriends = new ArrayList<>();
            for (int i = 0; i < friends.length(); i++) {
                JSONObject jsonFriends = friends.getJSONObject(i);
                try {
                    Friends contact = new Friends(jsonFriends);
                    listOfFriends.add(contact);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            mFriendsList.setValue(listOfFriends);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mFriendsList.setValue(mFriendsList.getValue());
    }


    public void connectGetFriends() {
        if (mUserModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + mUserModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + mUserModel.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    public void connectGetFriendsNotInChat() {
        if (mUserModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + mUserModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                //no body for this get request
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + mUserModel.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    public MutableLiveData<List<Friends>> getFriendsList() {
        return mFriendsList;
    }

    public void setUserInfoViewModel(UserInfoViewModel viewModel) {
        mUserModel = viewModel;
    }

    public List<Friends> getFriends() {
        return mFriendsList.getValue();
    }
}
