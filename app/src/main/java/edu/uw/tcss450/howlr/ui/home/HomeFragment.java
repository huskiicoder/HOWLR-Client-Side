package edu.uw.tcss450.howlr.ui.home;

import static edu.uw.tcss450.howlr.R.id.recycler_view_friends;
import static edu.uw.tcss450.howlr.R.id.recycler_view_messages;

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
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentHomeBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.friends.FriendsListRecyclerViewAdapter;
import edu.uw.tcss450.howlr.ui.friends.FriendsListViewModel;
import edu.uw.tcss450.howlr.ui.friends.HomeFriendsAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;
import edu.uw.tcss450.howlr.ui.messages.MessagesListViewModel;
import edu.uw.tcss450.howlr.ui.weather.Weather;
import edu.uw.tcss450.howlr.ui.weather.WeatherRecyclerViewAdapterDaily;
import edu.uw.tcss450.howlr.ui.weather.WeatherRecyclerViewAdapterHourly;
import edu.uw.tcss450.howlr.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private WeatherViewModel mWeatherModel;
    private UserInfoViewModel mUserInfo;

    /* List of users with chat. */
    List<MessageModel> mUserList;

    /* Recycler view adapter */
    HomeMessagesAdapter mAdapterMessages;

    /* View model for messages */
    MessagesListViewModel mMessagesModel;

    /* List of users */
    List<HomeFriendsModel> mFriendsList;

    /* Recycler view adapter for Friends */
    HomeFriendsAdapter mAdapterFriends;

    /* User view model. */
    UserInfoViewModel mUserModel;

    View myBinding;

    private FriendsListViewModel mFriendListModel;

    /**
     * Require empty public constructor
     */
    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserInfo = provider.get(UserInfoViewModel.class);
        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mWeatherModel.connectGet(mUserInfo.getmJwt());
        mUserModel = provider.get(UserInfoViewModel.class);
        mMessagesModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        mMessagesModel.connectGet(mUserModel.getmJwt(), mUserModel.getUserId());
        // Initializing friends list stuff
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
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        // TESTING MESSAGES STUFF
        myBinding = inflater.inflate(R.layout.fragment_home, container, false);
        return myBinding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);


        FragmentHomeBinding binding = FragmentHomeBinding.bind(getView());
        binding.accountName.setText(model.getEmail());

        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), list->{
            if (!list.isEmpty()){
                List<Weather> hourly_list = list.subList(1,25);
                List<Weather> daily_list = list.subList(26,33);
                int temp = (int) list.get(0).getCurrentTemp();
                binding.textTempHome.setText(temp + "°F");
                binding.textCityHome.setText(String.valueOf(list.get(0).getCity()));
            }
        });

        RecyclerView recyclerViewMessages = myBinding.findViewById(recycler_view_messages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        /**
         * Creates template data for recycler view.
         * TODO Delete after manual implementation of data is no longer needed.
         */
//        mUserList.add(new MessageModel(R.drawable.shibabone, 1, "Charles Bryan",
//                "2:30 pm", "Are you ready for the sprint review"));


        int messagesCount = 0;
        mUserList = new ArrayList<>();
        for (int i = 0; i < mMessagesModel.mMessagesList.getValue().size(); i++) {
            mUserList.add(mMessagesModel.mMessagesList.getValue().get(i));
            messagesCount++;
        }
        if(messagesCount < 4) {
            for(int i = 0; i < (4 - messagesCount); i++) {
                mUserList.add(new MessageModel(R.drawable.shibaheart, 1,
                        "Empty", "0:00 pm",
                        "Start a new chat, make some friends or whatever!"));
            }
        }

        mAdapterMessages = new HomeMessagesAdapter(mUserList);
        recyclerViewMessages.setAdapter(mAdapterMessages);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        // END MESSAGES IMPLEMENTATION

        // FOR FRIENDS RECYCLERVIEW
        RecyclerView recyclerViewFriends = myBinding.findViewById(recycler_view_friends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        /**
         * Creates template data for recycler view.
         * TODO Delete after manual implementation of data is no longer needed.
         */
        mFriendsList = new ArrayList<>();
//        mFriendsList.add(new HomeFriendsModel(R.drawable.shibabone, "Charles Bryan"));

        mFriendListModel.addFriendObserver(getViewLifecycleOwner(), friendsList -> {

            if (!friendsList.isEmpty()) {
                binding.recyclerViewFriends.setAdapter(new HomeFriendsAdapter(friendsList, this));
            }
        });

        recyclerViewFriends.setAdapter(mAdapterFriends);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());
    }
}