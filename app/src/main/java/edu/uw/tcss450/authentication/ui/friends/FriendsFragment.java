package edu.uw.tcss450.authentication.ui.friends;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.authentication.R;
import edu.uw.tcss450.authentication.databinding.FragmentFriendsBinding;
import edu.uw.tcss450.authentication.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private Friends mFriendModel;
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
        FragmentFriendsBinding binding = FragmentFriendsBinding.bind(getView());
        binding.textviewUsername.setText(mFriendModel.getUserName());
        binding.textviewFirstName.setText(mFriendModel.getFirstName());
        binding.textviewLastName.setText(mFriendModel.getLastName());
    }
}