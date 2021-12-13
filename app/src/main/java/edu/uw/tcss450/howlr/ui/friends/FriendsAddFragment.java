package edu.uw.tcss450.howlr.ui.friends;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsAddListBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * Implements fragment to add friends and search for users and send them a friend request.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsAddFragment extends Fragment implements View.OnClickListener {

    /**
     * The ViewModel for the friends list.
     */
    private FriendsListViewModel mFriendModel;

    /**
     * The ViewModel for the user's information.
     */
    private UserInfoViewModel mUserModel;

    /**
     * The adapter for adding users.
     */
    private ArrayAdapter<String> adapter;

    /**
     * The binding for adding users to the friends list.
     */
    FragmentFriendsAddListBinding binding;

    /**
     * Empty constructor for the add friends fraagment.
     */
    public FriendsAddFragment() {

    }

    /**
     * On the add friends fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendModel = new ViewModelProvider(getActivity()).get(FriendsListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mFriendModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mUserModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    /**
     * Creates the add friend fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsAddListBinding.inflate(inflater);
        return binding.getRoot();

    }

    /**
     * On the add friend fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AutoCompleteTextView editText = getView().findViewById(R.id.textView_addContact_friendsfragment);

        mFriendModel.addResultObserver(getViewLifecycleOwner(), result -> {
            editText.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, result));
        });

        binding.textViewAddContactFriendsfragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                //send new information to the server to update testSearch
                Log.d("CONTACTS", "searching for new string: " + s.toString());
                mFriendModel.connectSearchFriendsListGet(s.toString());
            }
        });
        binding.textViewAddContactFriendsfragment.setOnClickListener(this);
        mFriendModel.addResultObserver(getViewLifecycleOwner(), searchList -> {
            if (!searchList.isEmpty()) {
                binding.listRoot.setAdapter(new FriendsAddRecyclerViewAdapter(searchList, this));
            } else {
                binding.listRoot.setAdapter(new FriendsAddRecyclerViewAdapter(searchList, this));
                if (!binding.textViewAddContactFriendsfragment.getText().toString().isEmpty()) {
                    Context context = getContext();
                    CharSequence text = "NO RESULT FOUND";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    /**
     * The on-click action on the View.
     * @param v The View
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Adds a user using their email.
     * @param email The user's email
     */
    public void addFriend(final String email) {
        mFriendModel.connectAddFriendsPost(email);
    }
}