package edu.uw.tcss450.howlr.ui.messages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
        mModel = new MessagesListViewModel(requireActivity().getApplication());
        System.out.println("onCreate Size at constructor: " + mModel.mMessagesList.getValue().size());
        //mModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
        System.out.println("onCreate Size at connectGet: " + mModel.mMessagesList.getValue().size());
    }

/*    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mModel = new MessagesListViewModel(requireActivity().getApplication());
        System.out.println("onCreate Size at constructor: " + mModel.mMessagesList.getValue().size());
        //mModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt());
        System.out.println("onCreate Size at connectGet: " + mModel.mMessagesList.getValue().size());
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstancesState) {
        mBinding = inflater.inflate(R.layout.fragment_messages_page, container, false);
        RecyclerView recyclerView = mBinding.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        System.out.println("onCreate Size at onCreateView: " + mModel.mMessagesList.getValue().size());


        mUserList = new ArrayList<>();
        for (int i = 0; i < mModel.mMessagesList.getValue().size(); i++) {
            mUserList.add(mModel.mMessagesList.getValue().get(i));
        }
        System.out.println(mModel);

        /**
         * Creates template data for recycler view.
         * TODO Delete after manual implementation of data is no longer needed.
         */
      /*  mUserList.add(new MessageModel(R.drawable.shibabone, "Charles Bryan",
                "2:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibacoffee, "Amir Almemar",
                "3:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibadab, "Daniel Jiang",
                "4:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibadance, "Eddie Robinson",
                "5:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibaheart, "Justin Aschenbrenner",
                "6:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibalaptop, "Natalie Hong",
                "7:30 pm", "Are you ready for the sprint review")); */


        mAdapter = new MessageAdapter(mUserList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return mBinding;
    }

}