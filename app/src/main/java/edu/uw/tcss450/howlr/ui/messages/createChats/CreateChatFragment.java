package edu.uw.tcss450.howlr.ui.messages.createChats;


import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentCreateChatBinding;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsListBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.friends.Friends;
import edu.uw.tcss450.howlr.ui.friends.FriendsListFragmentDirections;
import edu.uw.tcss450.howlr.ui.friends.FriendsListRecyclerViewAdapter;
import edu.uw.tcss450.howlr.ui.friends.FriendsListViewModel;
import edu.uw.tcss450.howlr.ui.messages.MessageAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;
import edu.uw.tcss450.howlr.ui.messages.MessagesListViewModel;
import edu.uw.tcss450.howlr.ui.messages.MessagesPageFragmentDirections;

/**
 * create an instance of this fragment.
 */
public class CreateChatFragment extends Fragment {

    /* Messages view model. */
    CreateChatViewModel mModel;

    /* Recycler view adapter */
    CreateChatAdapter mAdapter;

    private FragmentCreateChatBinding mBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(CreateChatViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mModel.connectGet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = FragmentCreateChatBinding.inflate(inflater);

        mModel.addFriendObserver(getViewLifecycleOwner(), friendsList -> {
            if (!friendsList.isEmpty()) {
                mBinding.listRoot.setAdapter(new CreateChatAdapter(friendsList, this));
            }
        });

        return mBinding.getRoot();
    }

/*    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentCreateChatBinding binding = FragmentCreateChatBinding.bind(getView());

        mModel.addFriendObserver(getViewLifecycleOwner(), friendsList -> {
            if (!friendsList.isEmpty()) {
                binding.listRoot.setAdapter(new CreateChatAdapter(friendsList, this));
            }
        });

    }*/
}