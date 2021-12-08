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

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentMessagesPageBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.auth.signin.SignInFragmentDirections;


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
    View mBinding;

    /* Recycler view adapter */
    MessageAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt(), mUserModel.getmMemberId());
        System.out.println(mUserModel.getmMemberId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = inflater.inflate(R.layout.fragment_messages_page, container, false);
        RecyclerView recyclerView = mBinding.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mUserList = new ArrayList<>();
        for (int i = 0; i < mModel.mMessagesList.getValue().size(); i++) {
            mUserList.add(mModel.mMessagesList.getValue().get(i));
        }


        mAdapter = new MessageAdapter(getActivity().getApplicationContext(), mUserList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int itemClicked) {
                mUserModel.setChatRoom(mUserList.get(itemClicked).getChatId());
                Navigation.findNavController(getView())
                        .navigate(MessagesPageFragmentDirections
                                .actionNavigationMessagesToNavigationChat(mUserList.get(itemClicked).getChatId()));
            }
        });

        return mBinding;
    }

}