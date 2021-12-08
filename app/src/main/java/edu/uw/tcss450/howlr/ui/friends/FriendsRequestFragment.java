package edu.uw.tcss450.howlr.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.howlr.databinding.FragmentFriendsRequestCardBinding;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsRequestListBinding;

/**
 * Implements FriendsRequestFragment.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsRequestFragment extends Fragment implements View.OnClickListener{

    private FriendsListViewModel mFriendsListModel;

    FragmentFriendsRequestListBinding binding;

    public FriendsRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendsListModel = new ViewModelProvider(getActivity()).get(FriendsListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFriendsRequestListBinding.inflate(inflater);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentFriendsRequestListBinding binding = FragmentFriendsRequestListBinding.bind(getView());

        mFriendsListModel.addRequestListObserver(getViewLifecycleOwner(), requestList -> {
            binding.recyclerViewRequests.setAdapter(new edu.uw.tcss450.howlr.ui.friends.FriendsRequestRecyclerViewAdapter(requestList, this));
        });
    }


    @Override
    public void onClick(View v) {

    }

    public void deleteContact(final int memberId) {
        mFriendsListModel.connectDeleteContact(memberId);
    }

    public void acceptContact(final int memberId) {
        mFriendsListModel.connectAcceptFriends(memberId);
    }
}
