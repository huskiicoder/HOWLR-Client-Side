package edu.uw.tcss450.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.authentication.databinding.ActivityMainBinding;
import edu.uw.tcss450.authentication.model.NewFriendsCountViewModel;
import edu.uw.tcss450.authentication.model.UserInfoViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NewFriendsCountViewModel mNewFriendModel;
//    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
                ).get(UserInfoViewModel.class);

        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_messages, R.id.navigation_friends_list_fragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


//        mNewFriendModel = new ViewModelProvider(this).get(NewFriendsCountViewModel.class);
//
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            if (destination.getId() == R.id.navigation_friendsListFragment) {
//                //When the user navigates to the chats page, reset the new message count.
//                //This will need some extra logic for your project as it should have
//                //multiple chat rooms.
//                mNewFriendModel.reset();
//            }
//        });
//        mNewFriendModel.addFriendsCountObserver(this, count -> {
//            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_friends);
//            badge.setMaxCharacterCount(2);
//            if (count > 0) {
//                //new contact! update and show the notification badge.
//                badge.setNumber(count);
//                badge.setVisible(true);
//            } else {
//                //user did some action to clear the new contact, remove the badge
//                badge.clearNumber();
//                badge.setVisible(false);
//            }
//        });

    }
}
