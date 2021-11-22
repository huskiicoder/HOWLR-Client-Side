package edu.uw.tcss450.howlr.ui.home;

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
import edu.uw.tcss450.howlr.databinding.FragmentHomeBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.messages.MessageAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;

    /* List of users with chat. */
    List<MessageModel> mUserList;

    /* Recycler view adapter */
    MessageAdapter mAdapter;

    View myBinding;

    /**
     * Require empty public constructor
     */
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        // Instantiate binding object and inflate layout
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        // TESTING MESSAGES STUFF
        myBinding = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = (RecyclerView) myBinding.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /**
         * Creates template data for recycler view.
         * TODO Delete after manual implementation of data is no longer needed.
         */
        mUserList = new ArrayList<>();
        mUserList.add(new MessageModel(R.drawable.shibabone, "Charles Bryan",
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
                "7:30 pm", "Are you ready for the sprint review"));

        mAdapter = new MessageAdapter(mUserList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
    }
}
