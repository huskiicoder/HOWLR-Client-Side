package edu.uw.tcss450.authentication.ui.friends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.authentication.R;
import edu.uw.tcss450.authentication.databinding.FragmentFriendsBinding;
import edu.uw.tcss450.authentication.databinding.FragmentFriendsCardBinding;

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

        public FriendsListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsCardBinding.bind(view);
        }

        void setContact(final Friends friends) {

            binding.buttonDelete.setOnClickListener(view -> deleteContact(this, friends));
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewFirstName.setText(friends.getUserName());
            binding.textviewLastName.setText(friends.getLastName());

        }
    }

    private void deleteContact(final FriendsListViewHolder view, final Friends friends) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent.getActivity());
        builder.setTitle(R.string.dialog_friendsListRecycler_title);
        builder.setMessage(R.string.dialog_friendsListRecycler_message);
        builder.setPositiveButton(R.string.dialog_friendsListRecycler_confirm, (dialog, which) -> {
            mFriends.remove(friends);
            notifyItemRemoved(view.getLayoutPosition());
            final int memberId = friends.getMemberId();
            mParent.deleteFriend(memberId);
        });
        builder.setNegativeButton(R.string.dialog_friendsListRecycler_cancel, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
