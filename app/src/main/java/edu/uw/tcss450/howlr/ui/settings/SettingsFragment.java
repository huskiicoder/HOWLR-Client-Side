package edu.uw.tcss450.howlr.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentSettingsBinding;
import edu.uw.tcss450.howlr.databinding.FragmentSignInBinding;
import edu.uw.tcss450.howlr.ui.auth.signin.SignInFragmentDirections;

/**
 * Fragment for settings menu which can be accessed from any navigation
 * destination via top right corner. Settings menu allows users to change
 * application theme, as well as change password.
 * @author Edward Robinson, Amir Almemar,
 * @version TCSS 450 Fall 2021
 */
public class SettingsFragment extends Fragment {
    /** Radio button group for changing theme between light and dark mode */
    private RadioGroup modeRadioGroup;

    /** Binding for the Settings fragment */
    private FragmentSettingsBinding binding;


    /**
     * Required empty public constructor.
     */
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes the Fragment.
     *
     * @param savedInstanceState the instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Initializes the ViewBinding for Settings Fragment.
     *
     * @param inflater the inflater
     * @param container the ViewGroup container
     * @param savedInstanceState the saved instance state
     * @return the ViewBinding for Settings fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * Sets all the onClickListeners for buttons within settings fragment.
     *
     * @param view the view
     * @param savedInstanceState the instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modeRadioGroup = getView().findViewById(R.id.radio_group_mode);
        modeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_light) { Log.d("radio", "first");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if(checkedId == R.id.radio_dark) {
                    Log.d("radio", "blue");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        binding.buttonResetPassword.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SettingsFragmentDirections.actionNavigationSettingsToResetPasswordFragment2()
                ));
    }
}