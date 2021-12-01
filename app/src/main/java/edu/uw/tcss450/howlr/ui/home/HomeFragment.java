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

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentHomeBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.messages.MessageAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;
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

    /* List of users */
    List<HomeFriendsModel> mFriendsList;

    /* Recycler view adapter for Friends */
    HomeFriendsAdapter mAdapterFriends;

    View myBinding;

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
        mWeatherModel.connectGet(47,-122, mUserInfo.getmJwt());
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

        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), list->{
            if (!list.isEmpty()){
                List<Weather> hourly_list = list.subList(1,25);
                List<Weather> daily_list = list.subList(26,33);
                mBinding.textTemp.setText(String.valueOf(list.get(0).getCurrentTemp()));
                mBinding.textCity.setText(String.valueOf(list.get(0).getCity()) + "°");
            }
        });


        RecyclerView recyclerViewMessages = myBinding.findViewById(recycler_view_messages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        /**
         * Creates template data for recycler view.
         * TODO Delete after manual implementation of data is no longer needed.
         */
        mUserList = new ArrayList<>();
        mUserList.add(new MessageModel(R.drawable.shibabone, 1, "Charles Bryan",
                "2:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibacoffee, 1, "Amir Almemar",
                "3:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibadab, 1, "Daniel Jiang",
                "4:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibadance, 1, "Eddie Robinson",
                "5:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibaheart, 1, "Justin Aschenbrenner",
                "6:30 pm", "Are you ready for the sprint review"));
        mUserList.add(new MessageModel(R.drawable.shibalaptop, 1, "Natalie Hong",
                "7:30 pm", "Are you ready for the sprint review"));

        mAdapterMessages = new HomeMessagesAdapter(mUserList);
        recyclerViewMessages.setAdapter(mAdapterMessages);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());

        // FOR FRIENDS RECYCLERVIEW
        RecyclerView recyclerViewFriends = myBinding.findViewById(recycler_view_friends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        /**
         * Creates template data for recycler view.
         * TODO Delete after manual implementation of data is no longer needed.
         */
        mFriendsList = new ArrayList<>();
        mFriendsList.add(new HomeFriendsModel(R.drawable.shibabone, "Charles Bryan"));
        mFriendsList.add(new HomeFriendsModel(R.drawable.shibacoffee, "Charles Bryan"));
        mFriendsList.add(new HomeFriendsModel(R.drawable.shibalaptop, "Charles Bryan"));
        mFriendsList.add(new HomeFriendsModel(R.drawable.shibadab, "Charles Bryan"));
        mFriendsList.add(new HomeFriendsModel(R.drawable.shibaheart, "Charles Bryan"));

        mAdapterFriends = new HomeFriendsAdapter(mFriendsList);
        recyclerViewFriends.setAdapter(mAdapterFriends);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());


    }
}