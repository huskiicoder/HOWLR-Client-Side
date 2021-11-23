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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsBinding;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsListBinding;

public class FriendsListFragment extends Fragment {
    private FriendsListRecyclerViewAdapter mFriendAdapter;
    private FriendsListViewModel mFriendListModel;
    private FragmentFriendsListBinding binding;

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

        binding.imageButtonAddContactFriendsfragment.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(FriendsListFragmentDirections.actionNavigationFriendsListToNavigationFriendsRequest()));

        binding.imageButtonRequestContactFriendsfragment.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(FriendsListFragmentDirections.actionNavigationFriendsListToNavigationFriendsRequest()));

        mFriendListModel.addFriendObserver(getViewLifecycleOwner(), friendsList -> {

            if (!friendsList.isEmpty()) {
                binding.listRoot.setAdapter(new FriendsListRecyclerViewAdapter(friendsList, this));
            }
        });

    }

    public void deleteFriend(final int memberId) {
        mFriendListModel.connectDeleteContact(memberId);
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
////        binding = FragmentFriendsListBinding.inflate(inflater);
//        mBinding = inflater.inflate(R.layout.fragment_friends_list, container, false);
//        RecyclerView recyclerView = (RecyclerView) mBinding.findViewById(R.id.list_root);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mFriendsList = new ArrayList<>();
//        mFriendsList.add(new Friends(R.drawable.shibabone, 1, "cb3",
//                "Charles", "Bryan"));
//        mFriendsList.add(new Friends(R.drawable.shibacoffee, 2, "aa1",
//                "Amir", "Almemar"));
//        mFriendsList.add(new Friends(R.drawable.shibadab, 3, "dj5",
//                "Daniel", "Jiang"));
//        mFriendsList.add(new Friends(R.drawable.shibadance, 4, "er2",
//                "Eddie", "Eddie"));
//        mFriendsList.add(new Friends(R.drawable.shibaheart, 5, "ja7",
//                "Justin", "Aschenbrenner"));
//        mFriendsList.add(new Friends(R.drawable.shibalaptop, 6, "nat2",
//                "Natalie", "Hong"));
//
//        mFriendAdapter = new edu.uw.tcss450.howlr.ui.friends.FriendsListRecyclerViewAdapter(mFriendsList);
//        recyclerView.setAdapter(mFriendAdapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
////        return mBinding.getRoot();
//        return mBinding;
//    }
}
