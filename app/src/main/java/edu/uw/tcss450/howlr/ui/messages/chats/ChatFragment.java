package edu.uw.tcss450.howlr.ui.messages.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentChatBinding;
import edu.uw.tcss450.howlr.io.RequestQueueSingleton;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * Host for all components inside of the chat page.
 */
public class ChatFragment extends Fragment {

    /** The chat view model. */
    private ChatViewModel mChatModel;

    /** The user model */
    private UserInfoViewModel mUserModel;

    /** The chat send view model */
    private ChatSendViewModel mSendModel;

    /**
     * Empty constructor.
     */
    public ChatFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mChatModel.getFirstMessages(mUserModel.getChatRoom(), mUserModel.getmJwt());
        mSendModel = provider.get(ChatSendViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(requireView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(
                mChatModel.getMessageListByChatId(mUserModel.getChatRoom()),
                mUserModel.getEmail()));


        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() ->
                mChatModel.getNextMessages(mUserModel.getChatRoom(), mUserModel.getmJwt()));

        mChatModel.addMessageObserver(mUserModel.getChatRoom(), getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    Objects.requireNonNull(rv.getAdapter()).notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button ->
                mSendModel.sendMessage(mUserModel.getChatRoom(),
                mUserModel.getmJwt(),
                binding.editMessage.getText().toString()));
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));
    }

    /**
     * Sets up the invite friend button and leave chat button.
     * @param menu The menu that holds the buttons.
     */
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.action_invite_friend_button).setVisible(true);
        menu.findItem(R.id.action_leave_chat).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    /**
     * Performs certain actions depending on which button was selected.
     * @param item The item that was selected.
     * @return The item selected to the super method.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_invite_friend_button) {
            Navigation.findNavController(requireView())
                    .navigate(ChatFragmentDirections
                        .actionNavigationChatToAddFriendsFragment(mUserModel.getChatRoom()));
        } else if (item.getItemId() == R.id.action_leave_chat) {
            leaveChat();
            Navigation.findNavController(requireView())
                    .navigate(ChatFragmentDirections
                        .actionNavigationChatToNavigationMessages());
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the action to leave a chat.
     */
    public void leaveChat () {
        String url = "https://howlr-server-side.herokuapp.com/messages/leave";

        JSONObject body = new JSONObject();
        try {
            body.put("memberId", mUserModel.getmMemberId());
            body.put("chatId", mUserModel.getChatRoom());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                null,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mUserModel.getmJwt());
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(requireActivity().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Handles errors associated with leaving a chat.
     * @param error The volley error.
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

}