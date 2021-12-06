package edu.uw.tcss450.howlr.ui.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private List<MessageModel> mUserList;

    protected Context mContext;

    private OnItemClickListener mListener;

    /**
     * Constructor for the message adapter.
     * @param theUserList The list of people who the user is messaging with
     */
    public MessageAdapter(Context context, List<MessageModel> theUserList) {
        mContext = context;
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
        ViewHolder viewHolder = new ViewHolder(v, mListener);
        return viewHolder;
    }

    /**
     * Binds the data from the message activity to inside our RecyclerView layout.
     * @param holder The view holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        int picture = mUserList.get(position).getPicture();
        //int chatId = mUserList.get(position).getChatId();
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
        private ImageView mPicture;

        /** Chat Id. */
        private int mChatId;

        /** Display name. */
        private TextView mDisplayName;

        /** Message time. */
        private TextView mMessageTime;

        /** Message content. */
        private TextView mMessageContent;

        public MessageViewHolderClickListener mClickListener;

        /**
         * The view holder constructor.
         * @param itemView The item view
         */
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            mPicture = itemView.findViewById(R.id.picture);
            //mChatId = itemView.findViewById();
            mDisplayName = itemView.findViewById(R.id.display_name);
            mMessageTime = itemView.findViewById(R.id.message_time);
            mMessageContent = itemView.findViewById(R.id.message_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        /**
         * Click listener for entering chat room.
         */
        public interface MessageViewHolderClickListener {
            void onItemClick(int id);
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