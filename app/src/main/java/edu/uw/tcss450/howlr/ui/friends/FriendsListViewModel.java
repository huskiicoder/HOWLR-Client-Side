package edu.uw.tcss450.howlr.ui.friends;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * Implements FriendsListViewModel.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsListViewModel extends AndroidViewModel {

    /** MutableLiveData of List<Friends>.*/
    private MutableLiveData<List<Friends>> mFriends;

    /** MutableLiveData List of friend request. */
    private MutableLiveData<List<Friends>> mRequestList;

    /** MutableLiveData List of Json object. */
    private final MutableLiveData<JSONObject> mResponse;

    /** UserInfoViewModel object. */
    private UserInfoViewModel userInfoViewModel;

    /** MutableLiveData List of friend search in the friend list. */
    private MutableLiveData<List<Friends>> searchResult;

    /** MutableLiveData List of user from searching. */
    private MutableLiveData<List<Friends>> searchFriends;

    /**
     * Constructs FriendsListViewModel.
     * @param application
     */
    public  FriendsListViewModel(@NonNull Application application) {
        super(application);

        mFriends = new MutableLiveData<>();
        mFriends.setValue(new ArrayList<>());

        mRequestList = new MutableLiveData<>();
        mRequestList.setValue(new ArrayList<>());

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        searchResult = new MutableLiveData<>();
        searchResult.setValue(new ArrayList<>());

        searchFriends = new MutableLiveData<>();
        searchFriends.setValue(new ArrayList<>());
    }

    /**
     * Add friend list observer.
     * @param owner
     * @param observer
     */
    public  void addFriendObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<Friends>> observer) {
        mFriends.observe(owner, observer);
    }

    /**
     * Add friend request observer.
     * @param owner
     * @param observer
     */
    public void addRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Friends>> observer) {
        mRequestList.observe(owner, observer);
    }

    /**
     * Handles result for list of existing friends and invitation.
     * @param result
     */
    private void handleResult(final JSONObject result) {
        try {
            JSONObject root = result;

            boolean isSuccess = root.getBoolean("succes");
            if (!isSuccess) {
                return;
            }

            // Get list of invitations from jsonarray
            JSONArray request = root.getJSONArray("invitation");
            ArrayList<Friends> listOfInvites = new ArrayList<>();
            System.out.println(request.length());
            for(int i = 0; i < request.length(); i++) {
                JSONObject jsonFriends = request.getJSONObject(i);
                try {
                    Friends friends = new Friends(jsonFriends);
                    listOfInvites.add(friends);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // Get list of friends from jsonarray
            JSONArray friends = root.getJSONArray("contact");
            ArrayList<Friends> listOfFriends = new ArrayList<>();
            for (int i = 0; i < friends.length(); i++) {
                JSONObject jsonFriends = friends.getJSONObject(i);
                try {
                    Friends contact = new Friends(jsonFriends);
                    listOfFriends.add(contact);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ;
                }
            }
            // Set the value to the MutableLiveData List<Friends>
            mFriends.setValue(listOfFriends);
            mRequestList.setValue(listOfInvites);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mFriends.setValue(mFriends.getValue());
        mRequestList.setValue((mRequestList.getValue()));
    }

    /**
     * Handles error.
     * @param error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                            " " +
                            data);
        }
    }

    /**
     * Connect to webserver for getting contact list
     */
    public void connectGetAll() {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + userInfoViewModel.getEmail();
//        String url = "http://10.0.2.2:5000/contacts/" + userInfoViewModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Add searched friend list observer.
     * @param owner
     * @param observer
     */
    public void addSearchResultObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super List<Friends>> observer) {
        searchResult.observe(owner, observer);
    }

    /**
     * Add searched user list observer.
     * @param owner
     * @param observer
     */
    public void addResultObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super List<Friends>> observer) {
        searchFriends.observe(owner, observer);
    }

    /**
     * Handles the result of search an existing friend in the friend list.
     * @param result
     */
    private void handleSearchListResult(final JSONObject result) {
        try {
            JSONObject root = result;
            boolean isSuccess = root.getBoolean("succes");
            if (!isSuccess) {
                return;
            }
            JSONArray request = root.getJSONArray("rows");
            ArrayList<Friends> listOfInvites = new ArrayList<>();
            System.out.println(request.length());
            for(int i = 0; i < request.length(); i++) {
                JSONObject jsonFriends = request.getJSONObject(i);
                try {
                    Friends friends = new Friends(jsonFriends);
                    listOfInvites.add(friends);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            searchResult.setValue(listOfInvites);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        searchResult.setValue((searchResult.getValue()));
    }

    /**
     * Handles result of search an user.
     * @param result
     */
    private void handleAddSearchListResult(final JSONObject result) {
        try {
            JSONObject root = result;
            boolean isSuccess = root.getBoolean("succes");
            if (!isSuccess) {
                return;
            }
            JSONArray request = root.getJSONArray("rows");
            ArrayList<Friends> listOfInvites = new ArrayList<>();
            if (listOfInvites.isEmpty()) {

            }
            for(int i = 0; i < request.length(); i++) {
                JSONObject jsonFriends = request.getJSONObject(i);
                try {
                    Friends friends = new Friends(jsonFriends);
                    listOfInvites.add(friends);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            searchFriends.setValue(listOfInvites);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        searchFriends.setValue((searchFriends.getValue()));
    }

    /**
     * Connect to webserver for searching an existing friend in the friend list.
     * @param searchString
     */
    public void connectSearchFriendsGet(String searchString) {
        String url = "https://howlr-server-side.herokuapp.com/contacts/searchContact/" + userInfoViewModel.getEmail()
                + "/" + searchString;
//        String url = "http://10.0.2.2:5000/contacts/searchContact/" +
//                userInfoViewModel.getEmail() + "/" + searchString;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleSearchListResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Connect to webserver for searching an user.
     * @param searchString
     */
    public void connectSearchFriendsListGet(String searchString) {
        String url = "https://howlr-server-side.herokuapp.com/contacts/search/"
                + userInfoViewModel.getEmail() + "/" + searchString;
//        String url = "http://10.0.2.2:5000/contacts/search/" +
//                userInfoViewModel.getEmail() + "/" + searchString;

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleAddSearchListResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getmJwt());
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
     * Connect to webserver for sending a friend request.
     * @param email email of receiver
     */
    public void connectAddFriendsPost(String email) {
        String url = "https://howlr-server-side.herokuapp.com/contacts/";
//        String url = "http://10.0.2.2:5000/contacts/";

        JSONObject body = new JSONObject();
        try {
            body.put("usernameA", userInfoViewModel.getEmail());
            body.put("usernameB", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue, // we get a response but do nothing with it
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getmJwt());
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
     * Connect to webserver for accepting a friend request.
     * @param memberId memberid of the sender.
     */
    public void connectAcceptFriends(final int memberId) {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/accept/"
                + userInfoViewModel.getEmail() + "/" + memberId;
//        String url = "http://10.0.2.2:5000/contacts/accept/" + userInfoViewModel.getEmail() + "/" + memberId;
        Request request = new JsonObjectRequest(Request.Method.PUT, url, null,
                null, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", userInfoViewModel.getmJwt());

                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Handles result when deleting a friend contact.
     * @param result
     */
    private void handleDeleteResult(JSONObject result) {
        try {
            Log.d("FriendsListViewModel DELETE", "Result for delete attempt: " +
                    result.getString("success"));
        } catch (JSONException e) {
            throw new IllegalStateException("Unexpected response in FriendsListViewModel: " + result);
        }
    }

    /**
     * Connect to webserver for deleting a friend contact.
     * @param memberId memberid of the friend contact.
     */
    public void connectDeleteContact(final int memberId) {
        String url = "https://howlr-server-side.herokuapp.com/contacts/"
                    + userInfoViewModel.getEmail() + "/" + memberId;
//        String url = "http://10.0.2.2:5000/contacts/"+ userInfoViewModel.getEmail() + "/" + memberId;
        Request request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                this::handleDeleteResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", userInfoViewModel.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Set userinfoviewmodel.
     * @param viewModel
     */
    public void setUserInfoViewModel(UserInfoViewModel viewModel) {
        userInfoViewModel = viewModel;
    }

}
