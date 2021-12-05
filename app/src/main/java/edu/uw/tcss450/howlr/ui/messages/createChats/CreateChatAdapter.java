package edu.uw.tcss450.howlr.ui.messages.createChats;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsCardBinding;
import edu.uw.tcss450.howlr.ui.friends.Friends;
import edu.uw.tcss450.howlr.ui.friends.FriendsListFragment;
import edu.uw.tcss450.howlr.ui.friends.FriendsListFragmentDirections;
import edu.uw.tcss450.howlr.ui.friends.FriendsListRecyclerViewAdapter;

public class CreateChatAdapter extends RecyclerView.Adapter<CreateChatAdapter.CreateChatViewHolder> {

    /** The parent fragment. */
    private final CreateChatFragment mParent;

    /** The list of friends. */
    List<CreateChatFriendsModel> mFriends;

    /**
     * Instantiates the chat Adapter.
     * @param friends The list of friends for the recycler view.
     * @param parent The parent fragment.
     */
    public CreateChatAdapter(List<CreateChatFriendsModel> friends, CreateChatFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
    }

    /**
     * Inflates the CreateChatViewHolder.
     * @param parent The parent ViewGroup.
     * @param viewType The integer representation of viewType.
     * @return the inflated CreateChatViewHolder.
     */
    @NonNull
    @Override
    public CreateChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreateChatViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_create_chat_friends_model, parent, false));
    }

    /**
     * Binds the cards in the ViewHolder.
     * @param holder The CreateChatViewHolder.
     * @param position The position which represents which card is selected.
     */
    @Override
    public void onBindViewHolder(@NonNull CreateChatViewHolder holder, int position) {
        holder.setContact(mFriends.get(position));
    }

    @Override
    public int getItemCount() {

        return mFriends.size();
    }

    /**
     * Helper class that creates a view holder.
     */
    public class CreateChatViewHolder extends RecyclerView.ViewHolder {

        public final View mView;


        public FragmentFriendsCardBinding binding;

        private CreateChatFriendsModel mFriend;

        public CreateChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsCardBinding.bind(view);
        }

        void setContact(final CreateChatFriendsModel friends) {
            mFriend = friends;
            binding.textviewUsername.setText(friends.getUserName());
            binding.textviewFirstName.setText(friends.getFirstName());
            binding.textviewLastName.setText(friends.getLastName());
        }
    }
}
