package edu.uw.tcss450.howlr.ui.messages.createChats;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

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
    List<CreateChatFriendsModel> mFriendsList;

    /** List of friends selected to add to a chat */
    Set<Integer> mFriendsAddToChatList;

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
        mModel.connectGet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = inflater.inflate(R.layout.fragment_create_chat, container, false);
        RecyclerView recyclerView = mBinding.findViewById(R.id.create_chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFriendsList = new ArrayList<>();
        mFriendsList.addAll(Objects.requireNonNull(mModel.mFriendsList.getValue()));

        mFriendsAddToChatList = new HashSet<>();

        mAdapter = new CreateChatAdapter(requireActivity().getApplicationContext(), mFriendsList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        /* The click listener for individual recycler view items. */
        mAdapter.setOnItemClickListener(new CreateChatAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int itemClicked) {
                CreateChatFriendsModel selected = mFriendsList.get(itemClicked);
                if (!mFriendsAddToChatList.contains(selected.getMemberId())) {
                    mFriendsAddToChatList.add(selected.getMemberId());
                } else {
                    mFriendsAddToChatList.remove(selected.getMemberId());
                }
                if (!mFriendsAddToChatList.isEmpty()) {
                    mCreateChatConfirmButton.setImageResource(R.drawable.ic_create_chat_floatbutton_check_24);
                    mCreateChatConfirmButton.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
                } else {
                    mCreateChatConfirmButton.setImageResource(R.drawable.ic_create_chat_floatbutton_cancel_24);
                    mCreateChatConfirmButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
                System.out.println(mFriendsAddToChatList);
            }
        });

        /* The click listener for the confirm chat floating action button. */
        mCreateChatConfirmButton = (FloatingActionButton) mBinding.findViewById(R.id.createChatConfirmActionButton);
        mCreateChatConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("TEST FLOATING BUTTON");
            }
        });

        return mBinding;
    }
}