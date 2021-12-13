package edu.uw.tcss450.howlr.ui.auth.register;

import static edu.uw.tcss450.howlr.utils.PasswordValidator.*;
import static edu.uw.tcss450.howlr.utils.PasswordValidator.checkClientPredicate;

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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.howlr.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.howlr.utils.PasswordValidator;

/**
 * The fragment for the register page.
 */
public class RegisterFragment extends Fragment {

    /**
     * The binding for the register fragment.
     */
    private FragmentRegisterBinding binding;

    /**
     * The ViewModel for the register fragment.
     */
    private RegisterViewModel mRegisterModel;

    /**
     * Name validation for registering an account.
     * Checks if the name length is >= 1 character.
     */
    private PasswordValidator mNameValidator = checkPwdLength(1);

    /**
     * Email validation for registering an account.
     * Checks if the email length is >= 2 characters and contains an "@" symbol.
     */
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    /**
     * Password validation for registering an account.
     * Checks if the password:
     * - Length is >= 7 characters
     * - Contains at least 1 special character "@#$%&*!?"
     * - Excludes any white space characters
     * - Contains at least 1 digit character
     * - Contains at least 1 lowercase character
     * - Contains at least 1 uppercase character
     */
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Empty constructor for the register fragment.
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * On the register fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    /**
     * Creates the register fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    /**
     * On the register fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);

        // Handles pressing enter on keyboard after typing password to enter app right away
        // without use of button
        binding.editPassword2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView editPassword2, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("Note: ","Enter pressed");
                    binding.buttonRegister.performClick();
                }
                return false;
            }
        });

        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    /**
     * Attempts the register the user's account.
     * @param button The register button
     */
    private void attemptRegister(final View button) {
        validateFirst();
    }

    /**
     * The validation for the first name.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    /**
     * The validation for the last name.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    /**
     * The validation for the email.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    /**
     * The validation for the passwords matching.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    /**
     * The validation for the password.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password."));
    }

    /**
     * The validation for authentication with the server.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * The navigation to the login screen using the email and password from the registration page.
     */
    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server.
     * This observer should be attached to SignInViewModel.
     * @param response The Response from the server
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
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}