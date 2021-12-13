package edu.uw.tcss450.howlr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import edu.uw.tcss450.howlr.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

/**
 * The class used for authentication.
 */
public class AuthActivity extends AppCompatActivity {

    /**
     * On the authentication activity's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);

        initiatePushyTokenRequest();

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        getSupportActionBar().hide();
    }

    /**
     * Initiates the pushy token request.
     */
    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }
}