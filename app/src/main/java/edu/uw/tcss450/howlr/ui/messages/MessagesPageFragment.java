package edu.uw.tcss450.howlr.ui.messages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentMessagesPageBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.auth.signin.SignInFragmentDirections;
import edu.uw.tcss450.howlr.ui.friends.FriendsListRecyclerViewAdapter;


/**
 * The class for the messages page.
 */
public class MessagesPageFragment extends Fragment {

    /* Messages view model. */
    MessagesListViewModel mModel;

    /* User view model. */
    UserInfoViewModel mUserModel;

    /* List of users with chat. */
    List<MessageModel> mUserList;

    /* Binding to root */
    FragmentMessagesPageBinding mBinding;

    /* Recycler view adapter */
    MessageAdapter mAdapter;

    /** Button for creating a chat room. */
    FloatingActionButton mCreateChatButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mModel.connectGet(mUserModel.getmJwt(), mUserModel.getmMemberId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = FragmentMessagesPageBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMessagesPageBinding binding = FragmentMessagesPageBinding.bind(getView());

        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserList = new ArrayList<>();
        mUserList.addAll(Objects.requireNonNull(mModel.getMessagesList().getValue()));

        mAdapter = new MessageAdapter(getActivity().getApplicationContext(), mUserList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /* Click listener for navigating to chat from recycler view item. */
        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemClicked) {
                mUserModel.setChatRoom(mUserList.get(itemClicked).getChatId());
                Navigation.findNavController(getView())
                        .navigate(MessagesPageFragmentDirections
                                .actionNavigationMessagesToNavigationChat(mUserList.get(itemClicked).getChatId()));
            }
        });

        /* Click listener for navigating to CreateChatFragment using floating action button. */
        mCreateChatButton = (FloatingActionButton) binding.getRoot().findViewById(R.id.createChatActionButton);
        mCreateChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView())
                        .navigate(MessagesPageFragmentDirections
                                .actionNavigationMessagesToNavigationCreateChat());
            }
        });

    }

}