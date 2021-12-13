package edu.uw.tcss450.howlr.ui.home;

import static edu.uw.tcss450.howlr.R.id.recycler_home_weather;
import static edu.uw.tcss450.howlr.R.id.recycler_view_friends;
import static edu.uw.tcss450.howlr.R.id.recycler_view_messages;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentHomeBinding;
import edu.uw.tcss450.howlr.databinding.FragmentMessagesPageBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.friends.FriendsListViewModel;
import edu.uw.tcss450.howlr.ui.friends.HomeFriendsAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;
import edu.uw.tcss450.howlr.ui.messages.MessagesListViewModel;
import edu.uw.tcss450.howlr.ui.messages.MessagesPageFragmentDirections;
import edu.uw.tcss450.howlr.ui.weather.Weather;
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
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private LocationViewModel mModel;

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
        mModel = provider.get(LocationViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        mMessagesModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        mMessagesModel.connectGet(mUserModel.getJwt(), mUserModel.getMemberId());
        // Initializing friends list stuff
        mFriendListModel = new ViewModelProvider(getActivity()).get(FriendsListViewModel.class);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mFriendListModel.setUserInfoViewModel(activity.getUserInfoViewModel());
            mWeatherModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mFriendListModel.connectGetAll();
        mFriendListModel.connectGetFirstLast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        mWeatherModel.connectGet("47","-122", mUserModel.getJwt());
//        mWeatherModel.connectGet(Double.toString(mModel.getCurrentLocation().getLatitude()),
//                Double.toString(mModel.getCurrentLocation().getLongitude()), mUserModel.getmJwt());
        // TESTING MESSAGES STUFF
        myBinding = inflater.inflate(R.layout.fragment_home, container, false);
        return myBinding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        // get the users account name
        String email = model.getEmail();
        FragmentHomeBinding binding = FragmentHomeBinding.bind(getView());
        mFriendListModel.addFirstLastObserver(getViewLifecycleOwner(), nameList -> {
            Log.d("observer", "I entered firstLastObserver");
            if (!nameList.isEmpty()) {
                String first = nameList.get(0);
                String last = nameList.get(1);
                binding.accountName.setText(first + " " + last);
            }
        });

        // weather card building

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        date = dateFormat.format(calendar.getTime());
        binding.textWeatherDateHome.setText(date + " | ");
        // Recycler view set up for home weather card
        RecyclerView recyclerViewWeather = myBinding.findViewById(recycler_home_weather);
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));

        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), list->{
            if (!list.isEmpty()){
                List<Weather> daily_list = list.subList(26,31);
                int temp = (int) list.get(0).getCurrentTemp();
                binding.textTempHome.setText(temp + "°F");
                binding.textCityHome.setText(String.valueOf(list.get(0).getCity()));
                String a = "a" + list.get(0).getIcon();
                Context context = binding.imageHomeCurrWeather.getContext();
                int id = context.getResources().getIdentifier(a, "drawable", context.getPackageName());
                binding.imageHomeCurrWeather.setImageResource(id);
                recyclerViewWeather.setAdapter(new HomeWeatherAdapter(daily_list));
            }
        });

        mMessagesModel.addMessagesObserver(getViewLifecycleOwner(), messagesList -> {
            if (!messagesList.isEmpty()) {
                mAdapterMessages = new HomeMessagesAdapter(messagesList);
                mAdapterMessages.notifyDataSetChanged();
                binding.recyclerViewMessages.setAdapter(mAdapterMessages);

                /* Click listener for navigating to chat from recycler view item. */
                mAdapterMessages.setOnItemClickListener(itemClicked -> {
                    mUserModel.setChatRoom(messagesList.get(itemClicked).getChatId());
                    Navigation.findNavController(requireView())
                            .navigate(HomeFragmentDirections
                                    .actionNavigationHomeToNavigationChat(messagesList.get(itemClicked).getChatId()));
                });
            }
        });

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



        RecyclerView recyclerViewMessages = myBinding.findViewById(recycler_view_messages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        int messagesCount = 0;
        mUserList = new ArrayList<>();
        for (int i = 0; i < mMessagesModel.getMessagesList().getValue().size(); i++) {
            mUserList.add(mMessagesModel.getMessagesList().getValue().get(i));
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
    }
}