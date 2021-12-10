package edu.uw.tcss450.howlr.ui.friends;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsAddCardBinding;

/**
 * Implements FriendsAddRecyclerViewAdapter.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsAddRecyclerViewAdapter extends
        RecyclerView.Adapter<FriendsAddRecyclerViewAdapter.FriendsAddViewHolder> {

    private final FriendsAddFragment mParent;

    List<Friends> mFriends;

    private Context context;

    public FriendsAddRecyclerViewAdapter(List<Friends>  friends, FriendsAddFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
        context = parent.getContext();
    }

    @NonNull
    @Override
    public FriendsAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new FriendsAddViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_add_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAddViewHolder holder, int position) {

        holder.setFriend(mFriends.get(position));
    }

    @Override
    public int getItemCount() {

        return mFriends.size();
    }

    /**
     * Helper class that creates a view holder.
     */
    public class FriendsAddViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public FragmentFriendsAddCardBinding binding;

        private Friends mFriend;

        public FriendsAddViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsAddCardBinding.bind(view);
        }

        void setFriend(final Friends friends) {
//            binding.imgAvatar.setImageResource(friends.getPicture());
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewName.setText(friends.getFirstName() + " " + friends.getLastName());
            binding.buttonAddFriend.setOnClickListener(view -> addFriend(this, friends));
        }

        private void addFriend(final FriendsAddRecyclerViewAdapter.FriendsAddViewHolder view, Friends friends) {
            mFriends.remove(friends);
            notifyItemRemoved(view.getLayoutPosition());
            final String email = friends.getUserName();
            mParent.addFriend(email);

            CharSequence text = "You've Sent a Request!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Navigation.findNavController(mView).navigate(FriendsAddFragmentDirections.actionNavigationFriendsAddToNavigationFriendsList());
        }
    }
}