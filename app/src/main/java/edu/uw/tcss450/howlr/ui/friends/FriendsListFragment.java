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
 * Implements FriendsListFragment to display a list of friends.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */

public class FriendsListFragment extends Fragment {
    private FriendsListViewModel mFriendListModel;
    private FragmentFriendsListBinding binding;

    /**
     * An empty construct.
     */
    public FriendsListFragment() {

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsListBinding.inflate(inflater);

        return binding.getRoot();
    }


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
     * Delete a friend in the friend list.
     * @param memberId the member id.
     */
    public void deleteFriend(final int memberId) {
        mFriendListModel.connectDeleteContact(memberId);
    }
}
