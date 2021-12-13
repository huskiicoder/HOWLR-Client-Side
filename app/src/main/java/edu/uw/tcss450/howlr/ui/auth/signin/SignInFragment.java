package edu.uw.tcss450.howlr.ui.auth.signin;

import static edu.uw.tcss450.howlr.utils.PasswordValidator.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;


import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentSignInBinding;
import edu.uw.tcss450.howlr.model.PushyTokenViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.utils.PasswordValidator;

/**
 * The fragment for the sign in page.
 */
public class SignInFragment extends Fragment {

    /**
     * The binding for the sign in fragment.
     */
    private FragmentSignInBinding binding;

    /**
     * The ViewModel for the sign in fragment.
     */
    private SignInViewModel mSignInModel;

    /**
     * Email validation for signing in.
     * Checks if the email length is >= 2 characters and contains an "@" symbol.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Password validation for signing in.
     * Checks if the length is >= 1 character and contains no white spaces.
     */
    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    /**
     * The ViewModel for the pushy token.
     */
    private PushyTokenViewModel mPushyTokenViewModel;

    /**
     * The ViewModel for the user's information.
     */
    private UserInfoViewModel mUserViewModel;

    /**
     * Empty constructor for the sign in fragment.
     */
    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * On the sign in fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
    }

    /**
     * Creates the sign in fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * On the sign in fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionLoginFragmentToRegisterFragment()
                ));

        binding.buttonSignIn.setOnClickListener(this::attemptSignIn);

        binding.textForgotPassword.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToResetPasswordFragment()
                ));

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        //don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                binding.buttonSignIn.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    /**
     * Sends the pushy token to the web service.
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJwt());
    }

    /**
     * Tries to sign in with the email and password information.
     * @param button The sign in button
     */
    private void attemptSignIn(final View button) {
        validateEmail();
    }

    /**
     * The email validation for signing in and checking with the web service authentication.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * The password validation for signing in and checking with the web service authentication.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword.setError("Please enter a valid Password."));
    }

    /**
     * Verification with the web service authentication.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editEmail.getText().toString(),
                binding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Navigates the the home page on a successful login with authentication.
     * @param email The user's email
     * @param jwt The user's JSON web token
     */
    private void navigateToSuccess(final String email, final int memberid, final String jwt) {
        if (binding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
            prefs.edit().putInt(getString(R.string.keys_prefs_memberid), memberid).apply();
        }

        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionLoginFragmentToMainActivity(email,memberid, jwt));
        getActivity().finish();
    }

    /**
     * An observer on the HTTP Response from the web server.
     * This observer should be attached to SignInViewModel.=
     * @param response The response from the web server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    binding.editEmail.getText().toString(),
                                    response.getInt("memberid"),
                                    response.getString("token")
                            )).get(UserInfoViewModel.class);

                    sendPushyToken();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * On start after logging in.
     * This method also handles staying signed in after signing out of the account.
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
/*        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, mUserViewModel.getMemberId(), token);
                return;
            }
        }*/
    }

    /**
     * An observer on the HTTP Response from the web server.
     * This observer should be attached to PushyTokenViewModel.
     * @param response The response from the web server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        binding.editEmail.getText().toString(),
                        mUserViewModel.getMemberId(),
                        mUserViewModel.getJwt()
                );
            }
        }
    }
}