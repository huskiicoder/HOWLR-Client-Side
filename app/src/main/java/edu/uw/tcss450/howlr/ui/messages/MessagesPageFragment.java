package edu.uw.tcss450.howlr.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentMessagesPageBinding;
import edu.uw.tcss450.howlr.model.PushyTokenViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;


/**
 * The class for the messages page.
 */
public class MessagesPageFragment extends Fragment {

    /** Messages view model. */
    private MessagesListViewModel mModel;

    /** User view model. */
    private UserInfoViewModel mUserModel;

    /** List of users with chat. */
    private List<MessageModel> mUserList;

    /** Binding to root */
    private FragmentMessagesPageBinding mBinding;

    /** Recycler view adapter */
    private MessageAdapter mAdapter;

    /** Button for creating a chat room. */
    private FloatingActionButton mCreateChatButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(requireActivity()).get(MessagesListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mModel.connectGet(mUserModel.getJwt(), mUserModel.getMemberId());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = FragmentMessagesPageBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMessagesPageBinding binding = FragmentMessagesPageBinding.bind(requireView());

        mUserList = new ArrayList<>();
        mUserList.addAll(Objects.requireNonNull(mModel.getMessagesList().getValue()));

        mModel.addMessagesObserver(getViewLifecycleOwner(), messagesList -> {
            if (!messagesList.isEmpty()) {
                mAdapter = new MessageAdapter(messagesList);
                mAdapter.notifyDataSetChanged();
                binding.messagesPageRecyclerView.setAdapter(mAdapter);

                /* Click listener for navigating to chat from recycler view item. */
                mAdapter.setOnItemClickListener(itemClicked -> {
                    mUserModel.setChatRoom(messagesList.get(itemClicked).getChatId());
                    Navigation.findNavController(requireView())
                            .navigate(MessagesPageFragmentDirections
                                    .actionNavigationMessagesToNavigationChat(messagesList.get(itemClicked).getChatId()));
                });
            }
        });

        /* Click listener for navigating to CreateChatFragment using floating action button. */
        mCreateChatButton = (FloatingActionButton) binding.getRoot().findViewById(R.id.createChatActionButton);
        mCreateChatButton.setOnClickListener(view1 -> Navigation.findNavController(requireView())
                .navigate(MessagesPageFragmentDirections
                        .actionNavigationMessagesToNavigationCreateChat()));

    }

}