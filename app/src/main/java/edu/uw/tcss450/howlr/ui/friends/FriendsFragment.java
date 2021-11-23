package edu.uw.tcss450.howlr.ui.friends;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    public FriendsFragment() {
        // Require empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FriendsFragmentArgs args = FriendsFragmentArgs.fromBundle(getArguments());
        FragmentFriendsBinding binding = FragmentFriendsBinding.bind(getView());
        binding.textviewEmail.setText(args.getContact().getUserName());
        binding.textviewFirstName.setText(args.getContact().getFirstName());
        binding.textviewLastName.setText(args.getContact().getLastName());
    }
}