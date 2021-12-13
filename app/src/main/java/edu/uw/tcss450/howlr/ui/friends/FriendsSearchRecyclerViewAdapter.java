package edu.uw.tcss450.howlr.ui.friends;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsCardBinding;

/**
 * Implements RecyclerViewAdapter for searching in currently added friends.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsSearchRecyclerViewAdapter extends
        RecyclerView.Adapter<FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder> {

    /**
     * The friend search fragment.
     */
    private final FriendsSearchFragment mParent;

    /**
     * The list of friends.
     */
    List<Friends> mFriends;

    /**
     * The context.
     */
    private Context context;

    /**
     * Creates a RecyclerViewAdapter for searching friends.
     * @param friends The list of friends
     * @param parent The search friends fragment
     */
    public FriendsSearchRecyclerViewAdapter(List<Friends> friends, FriendsSearchFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
        context = parent.getContext();
    }

    /**
     * Creates the holder for the search friends fragment view.
     * @param parent The parent fragment
     * @param viewType The View type
     * @return The View holder
     */
    @NonNull
    @Override
    public FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_card, parent, false));
    }

    /**
     * The binding on the View holder.
     * @param holder The holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder holder, int position) {
        holder.setFriend(mFriends.get(position));
    }

    /**
     * Returns the number of results from searching friends.
     * @return The number of results from searching friends
     */
    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    /**
     * Class for creating a View holder for searching friends.
     */
    public class FriendsSearchViewHolder extends RecyclerView.ViewHolder {

        /**
         * The View.
         */
        public final View mView;

        /**
         * The binding for the friend cards.
         */
        public FragmentFriendsCardBinding binding;

        /**
         * Creates a View holder for the friend card.
         * @param view The View
         */
        public FriendsSearchViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsCardBinding.bind(view);
        }

        /**
         * Sets the friend to specific friends.
         * @param friends Specific friends
         */
        void setFriend(final Friends friends) {
//            binding.imgAvatar.setImageResource(friends.getPicture());
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewName.setText(friends.getFirstName() + " " + friends.getLastName());
            binding.buttonDelete.setOnClickListener(view -> deleteContact(this, friends));
        }
    }

    /**
     * Deletes the friend from the friend's list.
     * @param view The View
     * @param friend The friend
     */
    private void deleteContact(final FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder view, final Friends friend) {
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