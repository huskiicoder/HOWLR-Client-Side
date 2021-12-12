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
import java.util.function.IntFunction;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.friends.Friends;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;

/**
 * The view model for creating a chat.
 */
public class CreateChatViewModel extends AndroidViewModel {

    /** The mutable list of friends. */
    private MutableLiveData<List<Friends>> mFriendsList;

    /** The mutable response. */
    private final MutableLiveData<JSONObject> mResponse;

    /** The user view model. */
    private UserInfoViewModel mUserModel;

    /**
     * Instantiates the view model.
     * @param application The application.
     */
    public CreateChatViewModel(@NonNull Application application) {
        super(application);
        mFriendsList = new MutableLiveData<>();
        mFriendsList.setValue(new ArrayList<>());

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Adds a response observer to notify of any incoming changes to the friends list.
     * @param owner The lifecycle owner.
     * @param observer The observer.
     */
    public  void addFriendObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<Friends>> observer) {
        mFriendsList.observe(owner, observer);
    }

    /**
     * Handles fetching friends of the user.
     */
    public void connectGetFriends() {
        if (mUserModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + mUserModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mUserModel.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Handles fetching friends of the user not in a chat.
     */
    public void connectGetFriendsNotInChat() {
        if (mUserModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + mUserModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mUserModel.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Handles success from the friends fetch methods.
     * @param result The JSON result.
     */
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

    /**
     * Handles errors from the friends fetch methods.
     * @param error
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
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
