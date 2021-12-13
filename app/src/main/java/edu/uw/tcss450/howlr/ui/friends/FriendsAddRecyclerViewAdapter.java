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
 * Implements RecyclerViewAdapter for adding friends.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsAddRecyclerViewAdapter extends
        RecyclerView.Adapter<FriendsAddRecyclerViewAdapter.FriendsAddViewHolder> {

    /**
     * The add friends fragment.
     */
    private final FriendsAddFragment mParent;

    /**
     * The list of friends.
     */
    List<Friends> mFriends;

    /**
     * The context.
     */
    private Context context;

    /**
     * Creates a RecyclerViewAdapter for adding friends.
     * @param friends The list of friends
     * @param parent The add friends fragment
     */
    public FriendsAddRecyclerViewAdapter(List<Friends> friends, FriendsAddFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
        context = parent.getContext();
    }

    /**
     * Creates the holder for the add friends fragment view.
     * @param parent The parent fragment
     * @param viewType The View type
     * @return The View holder
     */
    @NonNull
    @Override
    public FriendsAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsAddViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_add_card, parent, false));
    }

    /**
     * The binding on the View holder.
     * @param holder The holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull FriendsAddViewHolder holder, int position) {
        holder.setFriend(mFriends.get(position));
    }

    /**
     * Returns the number of friends.
     * @return The number of friends.
     */
    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    /**
     * Class for creating a View holder for adding friends.
     */
    public class FriendsAddViewHolder extends RecyclerView.ViewHolder {

        /**
         * The View.
         */
        public final View mView;

        /**
         * The binding for the friend cards.
         */
        public FragmentFriendsAddCardBinding binding;

        /**
         * The friend.
         */
        private Friends mFriend;

        /**
         * Creates a View holder for the friend card.
         * @param view The View
         */
        public FriendsAddViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsAddCardBinding.bind(view);
        }

        /**
         * Sets the friend to specific friends.
         * @param friends Specific friends
         */
        void setFriend(final Friends friends) {
//            binding.imgAvatar.setImageResource(friends.getPicture());
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewName.setText(friends.getFirstName() + " " + friends.getLastName());
            binding.buttonAddFriend.setOnClickListener(view -> addFriend(this, friends));
        }

        /**
         * Adds a friend to the RecyclerView of friends.
         * @param view The View
         * @param friends The friend
         */
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