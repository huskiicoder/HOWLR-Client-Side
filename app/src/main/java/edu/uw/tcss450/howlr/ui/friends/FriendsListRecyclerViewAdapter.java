package edu.uw.tcss450.howlr.ui.friends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsCardBinding;
import edu.uw.tcss450.howlr.ui.weather.LocationFragmentDirections;

/**
 * Implements RecyclerViewAdapter for the friends list.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsListRecyclerViewAdapter extends
        RecyclerView.Adapter<FriendsListRecyclerViewAdapter.FriendsListViewHolder> {

    /**
     * The friends list fragment.
     */
    private final FriendsListFragment mParent;

    /**
     * The list of friends.
     */
    List<Friends> mFriends;

    /**
     * Creates a RecyclerViewAdapter for the friends list.
     * @param friends The list of friends
     * @param parent The friends list fragment
     */
    public FriendsListRecyclerViewAdapter(List<Friends> friends, FriendsListFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
    }

    /**
     * Creates the holder for the friends list fragment view.
     * @param parent The parent fragment
     * @param viewType The View type
     * @return The View holder
     */
    @NonNull
    @Override
    public FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsListViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_card, parent, false));
    }

    /**
     * The binding on the View holder.
     * @param holder The holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull FriendsListViewHolder holder, int position) {

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
     * Class for creating a View holder for the friends list.
     */
    public class FriendsListViewHolder extends RecyclerView.ViewHolder {

        /**
         * The View.
         */
        public final View mView;

        /**
         * The binding for the friend cards.
         */
        public FragmentFriendsCardBinding binding;

        /**
         * The friend.
         */
        private Friends mFriend;

        /**
         * Creates a View holder for the friend card.
         * @param view The View
         */
        public FriendsListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsCardBinding.bind(view);
        }

        /**
         * Sets the friend to specific friends.
         * @param friends Specific friends
         */
        void setFriend(final Friends friends) {

            mFriend = friends;
            binding.buttonDelete.setOnClickListener(view -> deleteContact(this,friends));
//            binding.imgAvatar.setImageResource(friends.getPicture());
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewName.setText(friends.getFirstName() + " " + friends.getLastName());
        }
    }

    /**
     * Deletes a friend from the friends list.
     * @param view The View
     * @param friend The friend
     */
    private void deleteContact(final FriendsListViewHolder view, final Friends friend) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent.getActivity());
        builder.setTitle("Delete Contact");
        builder.setMessage("Do you want to delete this contact?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            mFriends.remove(friend);
            notifyItemRemoved(view.getLayoutPosition());
            final int memberId = friend.getMemberId();
            mParent.deleteFriend(memberId);
        });
        builder.setNegativeButton("NO", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}