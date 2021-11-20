package edu.uw.tcss450.authentication.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.uw.tcss450.authentication.MainActivity;
import edu.uw.tcss450.authentication.databinding.FragmentFriendsBinding;
import edu.uw.tcss450.authentication.databinding.FragmentFriendsListBinding;

public class FriendsListFragment extends Fragment {
    private FriendsListViewModel mFriendListModel;
    private FragmentFriendsListBinding binding;

    public FriendsListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendListModel = new ViewModelProvider(getActivity()).get(FriendsListViewModel.class);
        //if (getActivity() instanceof MainActivity) {
        //    MainActivity activity = (MainActivity) getActivity();
        //    mFriendListModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        //}
        mFriendListModel.connectGet();
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

        binding.imageButtonAddContactFriendsfragment.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(FriendsListFragmentDirections.actionNavigationFriendsListFragmentToFriendsRequestFragment()));

        binding.imageButtonRequestContactFriendsfragment.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(FriendsListFragmentDirections.actionNavigationFriendsListFragmentToFriendsRequestFragment()));

        mFriendListModel.addFriendObserver(getViewLifecycleOwner(), friendsList -> {

            if (!friendsList.isEmpty()) {
                binding.listRoot.setAdapter(new FriendsListRecyclerViewAdapter(friendsList, this));
            }
        });

    }

    public void deleteFriend(final int memberId) {
        mFriendListModel.connectDeleteContact(memberId);
    }
}
