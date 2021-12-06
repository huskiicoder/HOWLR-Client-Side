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

import java.lang.reflect.Array;
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

public class FriendsListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Friends>> mFriends;
    private MutableLiveData<List<Friends>> mRequestList;
    private final MutableLiveData<JSONObject> mResponse;
    private UserInfoViewModel userInfoViewModel;
    private MutableLiveData<String[]> searchResult;
    private MutableLiveData<List<String>> mUsername;

    public  FriendsListViewModel(@NonNull Application application) {
        super(application);
        mFriends = new MutableLiveData<>();
        mFriends.setValue(new ArrayList<>());

        mUsername = new MutableLiveData<>();
        mUsername.setValue(new ArrayList<>());

        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());

        searchResult = new MutableLiveData<>();
        searchResult.setValue(new String[]{"null"});
    }

    public  void addFriendObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<Friends>> observer) {
        mFriends.observe(owner, observer);
    }

    public void addRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Friends>> observer) {
        mRequestList.observe(owner, observer);
    }

    private void handleResult(final JSONObject result) {
        IntFunction<String> getString =
                getApplication().getResources()::getString;
        try {
            JSONObject root = result;
                    JSONArray friends = root.getJSONArray("invitation");
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
                    mFriends.setValue(listOfFriends);
//                    mRequestList.setValue(listOfInvites);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mFriends.setValue(mFriends.getValue());
//        mRequestList.setValue((mRequestList.getValue()));
    }

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

    public void connectGetAll() {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/contacts/" + userInfoViewModel.getEmail();
        System.out.println(url);
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

    private void handleSearchResult(JSONObject result) {
        if (!result.has("rows")) {
            throw new IllegalStateException("Unexpected response in FriendsSearch: " + result);
        }
        try {
            Log.d("CONTACTS", "Results: " + result.toString());
            JSONArray rows = result.getJSONArray("rows");
            String[] stringResults = new String[rows.length()];

            for (int counter = 0; counter < rows.length(); counter++) {
                JSONObject row = rows.getJSONObject(counter);
                String username = row.getString("username");
                stringResults[counter] = username;
            }
            this.searchResult.setValue(stringResults);
            String log = "none";
            if (stringResults.length > 0) {
                log = stringResults[0];
            }
            Log.d("CONTACTS", "results for search in FriendsAddFragment: " + log);
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] getSearchResult() {
        return searchResult.getValue();
    }

    public void addSearchResultObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super String[]> observer) {
        this.searchResult.observe(owner, observer);
    }

    public void connectSearchFriendsGet(String stringToSearch) {
        String url = "https://howlr-server-side.herokuapp.com/"
                + "searchContacts?searchString=" + stringToSearch;
        Log.d("CONTACTS", "Results: " + stringToSearch);
        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleSearchResult, this::handleError) {
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

    public void connectAddFriendsPost(String email) {
        String url = "https://howlr-server-side.herokuapp.com/contacts";

        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
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

    public void connectAcceptFriends(final int memberId) {
        if (userInfoViewModel == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = "https://howlr-server-side.herokuapp.com/" +
                "contacts?memberId=" + memberId;
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

    public void connectDeleteContact(final int memberId) {
        String url = "https://howlr-server-side.herokuapp.com/contacts" +
                "?memberId=" + memberId;
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

    private void handleDeleteResult(JSONObject result) {
        try {
            Log.d("FriendsListViewModel DELETE", "Result for delete attempt: " +
                    result.getString("success"));
        } catch (JSONException e) {
            throw new IllegalStateException("Unexpected response in FriendsListViewModel: " + result);
        }
    }

    // THIS IS JUST TO GET THE CURRENT USERS FIRST AND LAST NAME
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

    public  void addFirstLastObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<String>> observer) {
        mUsername.observe(owner, observer);
    }


    public void setUserInfoViewModel(UserInfoViewModel viewModel) {
        userInfoViewModel = viewModel;
    }

}
