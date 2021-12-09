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
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Slide;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsSearchListBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * Implements FriendsSearchFragment to search for an existing friend contact in the friend list.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsSearchFragment extends Fragment implements View.OnClickListener {
    private FriendsListViewModel mFriendModel;
    private UserInfoViewModel mUserModel;
    private ArrayAdapter<String> adapter;
    FragmentFriendsSearchListBinding binding;

    public FriendsSearchFragment() {

    }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsSearchListBinding.inflate(inflater);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AutoCompleteTextView editText = getView().findViewById(R.id.editText_request_friends);

        mFriendModel.addSearchResultObserver(getViewLifecycleOwner(), result -> {
            editText.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, result));
        });

        binding.editTextRequestFriends.addTextChangedListener(new TextWatcher() {
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
                    mFriendModel.connectSearchFriendsGet(s.toString());
            }

        });
        binding.editTextRequestFriends.setOnClickListener(this);
        mFriendModel.addSearchResultObserver(getViewLifecycleOwner(), searchList -> {
            if (!searchList.isEmpty()) {
                binding.listRoot.setAdapter(new FriendsSearchRecyclerViewAdapter(searchList, this));
            } else {
                binding.listRoot.setAdapter(new FriendsSearchRecyclerViewAdapter(searchList, this));
                if (!binding.editTextRequestFriends.getText().toString().isEmpty()) {
                    Context context = getContext();
                    CharSequence text = "NO RESULT FOUND";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
    }
    /**
     * Delete a friend in the friend list.
     * @param memberId the member id.
     */
    public void deleteFriend(final int memberId) {
        mFriendModel.connectDeleteContact(memberId);
    }
}
