package edu.uw.tcss450.howlr.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsListBinding;

/**
 * The fragment for the friends list.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsListFragment extends Fragment {

    /**
     * The ViewModel for the friends list fragment.
     */
    private FriendsListViewModel mFriendListModel;

    /**
     * The binding for the friends list fragment.
     */
    private FragmentFriendsListBinding binding;

    /**
     * Empty constructor for the friends list fragment.
     */
    public FriendsListFragment() {

    }

    /**
     * On the friend list fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendListModel = new ViewModelProvider(getActivity()).get(FriendsListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mFriendListModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mFriendListModel.connectGetAll();
    }

    /**
     * Creates the friend list fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsListBinding.inflate(inflater);

        return binding.getRoot();
    }

    /**
     * On the friend list fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentFriendsListBinding binding = FragmentFriendsListBinding.bind(getView());

        binding.buttonInvite.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(FriendsListFragmentDirections.actionNavigationFriendsListToNavigationFriendsRequest()));
        binding.buttonAdd.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(FriendsListFragmentDirections.actionNavigationFriendsListToNavigationFriendsAdd()));
        binding.buttonSearch.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(FriendsListFragmentDirections.actionNavigationFriendsListToNavigationFriendsSearch()));

        mFriendListModel.addFriendObserver(getViewLifecycleOwner(), friendsList -> {

            if (!friendsList.isEmpty()) {
                binding.listRoot.setAdapter(new FriendsListRecyclerViewAdapter(friendsList, this));
            }
        });
    }

    /**
     * Deletes a friend using the friend's member ID.
     * @param memberId The friend's member ID
     */
    public void deleteFriend(final int memberId) {
        mFriendListModel.connectDeleteContact(memberId);
    }
}
