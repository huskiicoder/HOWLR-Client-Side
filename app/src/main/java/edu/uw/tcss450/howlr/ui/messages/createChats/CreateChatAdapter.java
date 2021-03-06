package edu.uw.tcss450.howlr.ui.messages.createChats;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.CreateChatItemDesignBinding;
import edu.uw.tcss450.howlr.ui.friends.Friends;

/**
 * Adapter Class that connects recycler view to the data.
 */
public class CreateChatAdapter extends RecyclerView.Adapter<CreateChatAdapter.CreateChatViewHolder> {

    /** The list of friends. */
    private final List<Friends> mFriends;

    /** Click listener for individual recycler view items. */
    private OnItemClickListener mListener;

    /**
     * Instantiates the chat Adapter.
     * @param friends The list of friends for the recycler view.
     */
    public CreateChatAdapter(List<Friends> friends) {
        this.mFriends = friends;
    }

    /**
     * Inflates the CreateChatViewHolder.
     * @param parent The parent ViewGroup.
     * @param viewType The integer representation of viewType.
     * @return the inflated CreateChatViewHolder.
     */
    @NonNull
    @Override
    public CreateChatAdapter.CreateChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_chat_item_design, parent, false);
        return new CreateChatViewHolder(v);
    }

    /**
     * Binds the cards in the ViewHolder.
     * @param holder The CreateChatViewHolder.
     * @param position The position which represents which card is selected.
     */
    @Override
    public void onBindViewHolder(@NonNull CreateChatViewHolder holder, int position) {
        holder.setContact(mFriends.get(position));

        /* Click listener that will check or uncheck the checkbox icon when selected. */
        holder.itemView.setOnClickListener(view -> {
            holder.setSelectedBox();
            mListener.OnItemClick(holder.getAbsoluteAdapterPosition());
        });
        if (mListener == null) System.out.println("NULL LISTENER");
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    /**
     * Interface for item clicks.
     */
    public interface OnItemClickListener {
        void OnItemClick(int itemClicked);
    }

    /**
     * Binds the click listener.
     * @param listener The listener to be binded.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Helper class that creates a view holder.
     */
    public static class CreateChatViewHolder extends RecyclerView.ViewHolder {

        private final CreateChatItemDesignBinding binding;

        /** Checkbox icon for being selected. */
        private final ImageView mSelectedIcon;

        /** Current selected state for the item. */
        private Boolean mSelected;


        /**
         * Instantiates the view holder and sets a click listener for clicking recycler view items.
         * @param itemView The item view.
         */
        public CreateChatViewHolder(View itemView) {
            super(itemView);
            binding = CreateChatItemDesignBinding.bind(itemView);
            mSelectedIcon = itemView.findViewById(R.id.create_chat_selected_check);
            mSelected = false;

        }

        /**
         * Sets the information for the holder.
         * @param friend The friend object.
         */
        void setContact(final Friends friend) {
            binding.textviewUsername.setText(friend.getUserName());
            binding.textviewName.setText(friend.getFirstName() + " " + friend.getLastName());
            mSelectedIcon.setImageResource(R.drawable.ic_create_chat_friend_blank_checkbox_24);
        }

        /**
         * Sets the selected check box based on if the item has been clicked on already.
         */
        void setSelectedBox() {
            if (!mSelected) {
                mSelectedIcon.setImageResource(R.drawable.ic_create_chat_friend_selected_checkbox_24);
                mSelected = true;
            } else {
                mSelectedIcon.setImageResource(R.drawable.ic_create_chat_friend_blank_checkbox_24);
                mSelected = false;
            }
        }
    }
}
