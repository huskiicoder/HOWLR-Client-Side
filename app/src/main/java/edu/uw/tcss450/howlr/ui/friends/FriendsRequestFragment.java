package edu.uw.tcss450.howlr.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsRequestCardBinding;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsRequestListBinding;

/**
 * The fragment for the friend request page.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsRequestFragment extends Fragment implements View.OnClickListener{

    /**
     * The ViewModel for the friend request fragment.
     */
    private FriendsListViewModel mFriendsListModel;

    /**
     * The binding for the friend request fragment.
     */
    FragmentFriendsRequestListBinding binding;

    /**
     * Empty constructor for the friend request fragment.
     */
    public FriendsRequestFragment() {
        // Required empty public constructor
    }

    /**
     * On the friend request fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendsListModel = new ViewModelProvider(getActivity()).get(FriendsListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mFriendsListModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mFriendsListModel.connectGetAll();
    }

    /**
     * Creates the friend request fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsRequestListBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * On the friend request fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentFriendsRequestListBinding binding = FragmentFriendsRequestListBinding.bind(getView());

        mFriendsListModel.addRequestListObserver(getViewLifecycleOwner(), requestList -> {
            binding.recyclerViewRequests.setAdapter(new FriendsRequestRecyclerViewAdapter(requestList, this));
        });
    }

    /**
     * On click action for the View.
     * @param v The View.
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Decline an incoming friend request.
     * @param memberId The incoming user's member ID
     */
    public void declineContact(final int memberId) {
        mFriendsListModel.connectDeleteContact(memberId);
    }

    /**
     * Accept an incoming friend request.
     * @param memberId The incoming user's member ID
     */
    public void acceptContact(final int memberId) {
        mFriendsListModel.connectAcceptFriends(memberId);
    }
}