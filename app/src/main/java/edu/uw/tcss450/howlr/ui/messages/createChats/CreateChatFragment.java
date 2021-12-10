package edu.uw.tcss450.howlr.ui.messages.createChats;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.friends.Friends;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;
import edu.uw.tcss450.howlr.ui.messages.MessagesPageFragmentDirections;

/**
 * create an instance of this fragment.
 */
public class CreateChatFragment extends Fragment {

    /** Messages view model. */
    CreateChatViewModel mModel;

    /** User view model. */
    UserInfoViewModel mUserModel;

    /** Recycler view adapter */
    CreateChatAdapter mAdapter;

    /** List of friends. */
    List<Friends> mFriendsList;

    /** List of friends selected to add to a chat */
    List<Integer> mFriendsAddToChatList;

    /** The binding to the view. */
    private View mBinding;

    /** Button for confirming creation of a chat room. */
    FloatingActionButton mCreateChatConfirmButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(CreateChatViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(CreateChatViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mModel.connectGetFriends();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = inflater.inflate(R.layout.fragment_create_chat, container, false);
        RecyclerView recyclerView = mBinding.findViewById(R.id.create_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFriendsList = new ArrayList<>();
        mFriendsList.addAll(Objects.requireNonNull(mModel.mFriendsList.getValue()));

        mFriendsAddToChatList = new ArrayList<>();

        mAdapter = new CreateChatAdapter(requireActivity().getApplicationContext(), mFriendsList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        /* The click listener to add friends to new chat hashset. */
        mAdapter.setOnItemClickListener(new CreateChatAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int itemClicked) {
                Friends selected = mFriendsList.get(itemClicked);
                if (!mFriendsAddToChatList.contains(selected.getMemberId())) {
                    mFriendsAddToChatList.add(selected.getMemberId());
                } else {
                    for (int i = 0; i < mFriendsAddToChatList.size(); i++) {
                        if (mFriendsAddToChatList.get(i) == selected.getMemberId()) {
                            mFriendsAddToChatList.remove(i);
                        }
                    }
                }
                if (!mFriendsAddToChatList.isEmpty()) {
                    mCreateChatConfirmButton.setImageResource(R.drawable.ic_create_chat_floatbutton_check_24);
                    mCreateChatConfirmButton.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                } else {
                    mCreateChatConfirmButton.setImageResource(R.drawable.ic_create_chat_floatbutton_cancel_24);
                    mCreateChatConfirmButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
                System.out.println(mUserModel.getmMemberId() + " " + mFriendsAddToChatList);
            }
        });

        /* The click listener for the confirm chat floating action button. */
        mCreateChatConfirmButton = (FloatingActionButton) mBinding.findViewById(R.id.createChatConfirmActionButton);
        mCreateChatConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFriendsAddToChatList.isEmpty()) {
                    createChatRoom();
                }
            }
        });

        return mBinding;
    }

    private void createChatRoom() {

        mFriendsAddToChatList.add(mUserModel.getmMemberId());
        JSONArray list = new JSONArray();
        for (int i = 0; i < mFriendsAddToChatList.size(); i++) {
            list.put(mFriendsAddToChatList.get(i));
        }
        JSONObject body = new JSONObject();
        try {
            body.put("name", "Default");
            body.put("list", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = "https://howlr-server-side.herokuapp.com/chats/create";
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", mUserModel.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getActivity().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleResult(final JSONObject result) {
        try {
            int navToChat = result.getInt("chatID");
            System.out.println("CHAT ROOM" + navToChat);

            mUserModel.setChatRoom(navToChat);
            Navigation.findNavController(getView())
                    .navigate(CreateChatFragmentDirections
                            .actionNavigationCreateChatToNavigationChat(navToChat));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }

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

}