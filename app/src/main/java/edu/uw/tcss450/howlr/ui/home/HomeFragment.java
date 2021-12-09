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
import edu.uw.tcss450.howlr.ui.messages.MessagesListViewModel;
import edu.uw.tcss450.howlr.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private WeatherViewModel mWeatherModel;

    /* List of users with chat. */
    List<MessageModel> mUserList;

    /* Recycler view adapter */
    HomeMessagesAdapter mAdapterMessages;

    /* List of users */
    List<HomeFriendsModel> mFriendsList;

    /* Recycler view adapter for Friends */
    HomeFriendsAdapter mAdapterFriends;

    View myBinding;

    /* Messages view model. */
    MessagesListViewModel mModel;

    /* User view model. */
    UserInfoViewModel mUserModel;

    /**
     * Require empty public constructor
     */
    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(MessagesListViewModel.class);
        mModel.connectGet(mUserModel.getmJwt(), mUserModel.getUserId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mWeatherModel.connectGetCurrent();

        // TESTING MESSAGES STUFF
        myBinding = inflater.inflate(R.layout.fragment_home, container, false);
        return myBinding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
//        try {
//            mBinding.textCity.setText((String)mWeatherModel.mResponse.getValue().get("city_name")
//                    + ", " + (String) mWeatherModel.mResponse.getValue().get("state_code"));
//            int temp = (int) mWeatherModel.mResponse.getValue().get("temp");
//            temp = (9/5) * temp +32;
//            mBinding.textTemp.setText(mWeatherModel.mResponse.getValue().get("temp") + "°");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


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