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
 * Implements FriendsSearchRecyclerViewAdapter.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsSearchRecyclerViewAdapter extends
        RecyclerView.Adapter<FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder> {

    private final FriendsSearchFragment mParent;

    List<Friends> mFriends;

    private Context context;

    public FriendsSearchRecyclerViewAdapter(List<Friends>  friends, FriendsSearchFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
        context = parent.getContext();
    }

    @NonNull
    @Override
    public FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsSearchRecyclerViewAdapter.FriendsSearchViewHolder holder, int position) {

        holder.setFriend(mFriends.get(position));
    }

    @Override
    public int getItemCount() {

        return mFriends.size();
    }

    /**
     * Helper class that creates a view holder.
     */
    public class FriendsSearchViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public FragmentFriendsCardBinding binding;

        public FriendsSearchViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsCardBinding.bind(view);
        }

        void setFriend(final Friends friends) {
//            binding.imgAvatar.setImageResource(friends.getPicture());
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewName.setText(friends.getFirstName() + " " + friends.getLastName());
            binding.buttonDelete.setOnClickListener(view -> deleteContact(this, friends));
        }
    }

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
