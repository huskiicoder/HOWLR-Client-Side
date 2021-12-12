package edu.uw.tcss450.howlr.ui.messages;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * The view model for the messages list.
 */
public class MessagesListViewModel extends AndroidViewModel  {

    /** The live mutable list of messages */
    private final MutableLiveData<List<MessageModel>> mMessagesList;

    /** The user view model. */
    private UserInfoViewModel userInfoViewModel;

    /**
     * Instantiates the view model.
     * @param application The application.
     */
    public MessagesListViewModel(@NonNull Application application) {
        super(application);
        mMessagesList = new MutableLiveData<>();
        mMessagesList.setValue(new ArrayList<>());
    }

    /**
     * Adds a response observer to notify of any incoming changes to the friends list.
     * @param owner The lifecycle owner.
     * @param observer The observer.
     */
    public  void addMessagesObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super  List<MessageModel>> observer) {
        mMessagesList.observe(owner, observer);
    }

    /**
     * Handles fetching the list of messages that the user is associated with.
     * @param jwt The JSON Web Token.
     * @param memberId The users ID.
     */
    public void connectGet(final String jwt, final int memberId) {
        String url = "https://howlr-server-side.herokuapp.com/chats/" + memberId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

    }

    /**
     * Handles success from connectGet.
     * @param result The JSON result.
     */
    private void handleResult(final JSONObject result) {
        try {
            JSONArray messages = result.getJSONArray("rows");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject jsonMessage = messages.getJSONObject(i);
                MessageModel cm = new MessageModel(
                        R.drawable.shibabone,
                        jsonMessage.getInt("chatid"),
                        jsonMessage.getString("firstname") +
                                " " + jsonMessage.getString("lastname"),
                        jsonMessage.getString("timestamp"),
                        jsonMessage.getString("message"));
                if (!Objects.requireNonNull(mMessagesList.getValue()).contains(cm)) {
                    mMessagesList.getValue().add(cm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
        mMessagesList.setValue(mMessagesList.getValue());
    }

    /**
     * Handles errors from connectGet.
     * @param error The volley error.
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    public MutableLiveData<List<MessageModel>> getMessagesList() {
        return mMessagesList;
    }

    public void setUserInfoViewModel(UserInfoViewModel viewModel) { userInfoViewModel = viewModel; }

}
