package edu.uw.tcss450.howlr.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentResetPasswordBinding;
import edu.uw.tcss450.howlr.databinding.FragmentSignInBinding;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.auth.register.RegisterViewModel;
import edu.uw.tcss450.howlr.ui.auth.signin.SignInFragmentDirections;
/**
 * The fragment for the resetting the user's password.
 */
public class ResetPasswordFragment extends Fragment {
    private FragmentResetPasswordBinding binding;
    private ResetViewModel mModel;

    /**
     * Empty constructor for the password reset fragment.
     */
    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * On the password reset fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(ResetViewModel.class);
    }

    /**
     * Creates the password reset's fragment view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * On the password reset's fragment view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSubmitReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mModel.connect(binding.editEmailReset.getText().toString().trim());
                String text = "Email sent!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
                Navigation.findNavController(getView()).navigate(
                        ResetPasswordFragmentDirections.actionResetPasswordFragmentToSignInFragment()
                );
            }
        });
    }

    /**
     * Confirms the password reset email was sent.
     * @param email The entered email
     */
    private void confirmEmailSent(final String email) {

    }
}