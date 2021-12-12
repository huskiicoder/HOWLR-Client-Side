package edu.uw.tcss450.howlr.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;

/**
 * Adapter class that connects the RecyclerView to the data.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    /**
     * The list of people who the user is messaging with.
     */
    private final List<MessageModel> mUserList;

    /** The adapter click listener. */
    private OnItemClickListener mListener;

    /**
     * Constructor for the message adapter.
     * @param theUserList The list of people who the user is messaging with
     */
    public MessageAdapter(List<MessageModel> theUserList) {
        this.mUserList = theUserList;
    }

    /**
     * Inflates the design of our item design.
     * @param parent The parent
     * @param viewType The view type
     * @return The view holder
     */
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_page_item_design, parent, false);
        return new ViewHolder(v, mListener);
    }

    /**
     * Binds the data from the message activity to inside our RecyclerView layout.
     * @param holder The view holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        int picture = mUserList.get(position).getPicture();
        String displayName = mUserList.get(position).getRecentName();
        String messageTime = mUserList.get(position).getMessageTime();
        String messageContent = mUserList.get(position).getMessageContent();

        holder.setData(picture, displayName, messageTime, messageContent);
    }

    /**
     * The list size.
     * @return The list size
     */
    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int itemClicked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * ViewHolder class.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** Profile picture. */
        private final ImageView mPicture;

        /** Display name. */
        private final TextView mDisplayName;

        /** Message time. */
        private final TextView mMessageTime;

        /** Message content. */
        private final TextView mMessageContent;

        /**
         * The view holder constructor.
         * @param itemView The item view
         */
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.picture);
            mDisplayName = itemView.findViewById(R.id.display_name);
            mMessageTime = itemView.findViewById(R.id.message_time);
            mMessageContent = itemView.findViewById(R.id.message_content);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        /**
         * Sets the data for the message card.
         * @param thePicture The profile picture
         * @param theDisplayName The display name
         * @param theMessageTime The message time
         * @param theMessageContent The message content
         */
        public void setData(int thePicture, String theDisplayName,
                            String theMessageTime, String theMessageContent) {
            mPicture.setImageResource(thePicture);
            mDisplayName.setText(theDisplayName);
            mMessageTime.setText(theMessageTime);
            mMessageContent.setText(theMessageContent);
        }
    }
}