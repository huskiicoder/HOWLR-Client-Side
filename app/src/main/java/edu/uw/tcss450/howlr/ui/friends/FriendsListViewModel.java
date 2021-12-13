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

import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * The ViewModel for the register fragment for the friends list.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsListViewModel extends AndroidViewModel {

    /**
     * The liva data from a list of friends.
     */
    private MutableLiveData<List<Friends>> mFriends;

    /**
     * The liva data from a list of friend requests.
     */
    private MutableLiveData<List<Friends>> mRequestList;

    /**
     * The response of JSON objects for the live data.
     */
    private final MutableLiveData<JSONObject> mResponse;

    /**
     * The ViewModel of the user's information.
     */
    private UserInfoViewModel userInfoViewModel;

    /**
     * The liva data from a list of users on a search results.
     */
    private MutableLiveData<List<Friends>> searchResult;

    /**
     * The liva data from a list of friends on a search request.
     */
    private MutableLiveData<List<Friends>> searchFriends;

    /**
     * The liva data from a list of usernames.
     */
    private MutableLiveData<List<String>> mUsername;

    /**
     * Creates the ViewModel for the friends list fragment given an application.
     * @param application The application
     */
    public FriendsListViewModel(@NonNull Application application) {
        super(application);

        mFriends = new MutableLiveData<>();
        mFriends.setValue(new ArrayList<>());

        mRequestList = new MutableLiveData<>();
        mRequestList.setValue(new ArrayList<>());

        mUsername = new MutableLiveData<>();
        mUsername.setValue(new ArrayList<>());

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        searchResult = new MutableLiveData<>();
        searchResult.setValue(new ArrayList<>());

        searchFriends = new MutableLiveData<>();
        searchFriends.setValue(new ArrayList<>());
    }

    /**
     * Adds an observer to the friends list fragment for adding friends.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addFriendObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<Friends>> observer) {
        mFriends.observe(owner, observer);
    }

    /**
     * Adds an observer to the friends list fragment for incoming friend requests.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Friends>> observer) {
        mRequestList.observe(owner, observer);
    }

    /**
     * Handles the result from successfully adding friends and getting friend requests.
     * @param result The result.
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
     * Handles the error for the HTTP library Volley.
     * @param error The error
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
     * Connects to the web server for getting the friends list.
     */
    public void connectGetAll() {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + userInfoViewModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                //no body for this get request
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
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
     * Adds an observer to the search results.
     * @param owner The owner
     * @param observer The observer
     */
    public void addSearchResultObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super List<Friends>> observer) {
        searchResult.observe(owner, observer);
    }

    /**
     * Adds an observer to the results.
     * @param owner The owner
     * @param observer The observer
     */
    public void addResultObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super List<Friends>> observer) {
        searchFriends.observe(owner, observer);
    }

    /**
     * Handles the search result list from the JSON object.
     * @param result The result
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
     * Handles the add to the search result list from the JSON object.
     * @param result The result
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
     * Connects to the web server for searching existing friends in the friends list.
     * @param searchString The search string
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
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
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
     * Connects to the web server for searching existing friends in the friends list.
     * @param searchString The search string
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
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
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
     * Connects to the web server for sending a friend request.
     * @param email The email of the friend that you want to add
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
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
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
     * Connects to the web server for accepting incoming friend requests.
     * @param memberId The member ID of the friend that is adding you
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
                headers.put("Authorization", userInfoViewModel.getJwt());

                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Handles the delete result from deleting friends.
     * @param result The result
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
     * Connects to the web server for deleting a friend from the friends list.
     * @param memberId The member ID of the friend to be deleted from the friends list
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
                headers.put("Authorization", userInfoViewModel.getJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * Gets the first name and last name of the current users.
     */
    public void connectGetFirstLast() {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/name/" +
                userInfoViewModel.getEmail();
        Request request = new JsonObjectRequest(Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleFirstLastResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", "Bearer " + userInfoViewModel.getJwt());
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
     * Handles the result of getting the first name and last name of the users.
     * @param result The result.
     */
    private void handleFirstLastResult(final JSONObject result) {
        try {
            JSONObject root = result;
            JSONArray name = root.getJSONArray("rows");
            ArrayList<String> fullName = new ArrayList<>();
            try {
                JSONObject firstJSON = name.getJSONObject(0);
//                JSONObject lastJSON = name.getJSONObject(1);
                String firstName = (String) firstJSON.get("firstname");
                String lastName = (String)firstJSON.get("lastname");
                fullName.add(firstName);
                fullName.add(lastName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mUsername.setValue(fullName);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mUsername.setValue(mUsername.getValue());
    }

    /**
     * Adds an observer to the first name and last name of a user.
     * @param owner The owner
     * @param observer The observer
     */
    public void addFirstLastObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<String>> observer) {
        mUsername.observe(owner, observer);
    }

    /**
     * Sets the ViewModel for a user's information.
     * @param viewModel The ViewModel
     */
    public void setUserInfoViewModel(UserInfoViewModel viewModel) {
        userInfoViewModel = viewModel;
    }
}