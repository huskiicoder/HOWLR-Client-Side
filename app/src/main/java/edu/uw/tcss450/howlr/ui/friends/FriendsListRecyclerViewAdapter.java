package edu.uw.tcss450.howlr.ui.friends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsCardBinding;

/**
 * Implements FriendsListRecyclerViewAdapter
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsListRecyclerViewAdapter extends
        RecyclerView.Adapter<FriendsListRecyclerViewAdapter.FriendsListViewHolder> {

    private final FriendsListFragment mParent;

    List<Friends> mFriends;

    public FriendsListRecyclerViewAdapter(List<Friends> friends, FriendsListFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
    }

    @NonNull
    @Override
    public FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new FriendsListViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListViewHolder holder, int position) {

        holder.setContact(mFriends.get(position));
    }

    @Override
    public int getItemCount() {

        return mFriends.size();
    }

    /**
     * Helper class that creates a view holder.
     */
    public class FriendsListViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public FragmentFriendsCardBinding binding;

        private Friends mFriend;

        public FriendsListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsCardBinding.bind(view);
        }

        void setContact(final Friends friends) {

            mFriend = friends;
            binding.cardRoot.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(FriendsListFragmentDirections.actionNavigationFriendsListToNavigationFriends(friends));
            });
//            binding.imgAvatar.setImageResource(friends.getPicture());
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewFirstName.setText(friends.getFirstName());
            binding.textviewLastName.setText(friends.getLastName());
        }
    }
}
