package edu.uw.tcss450.howlr.ui.home;

import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.ui.messages.MessageAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageModel;

/**
 * Adapter class that connects the RecyclerView to the data.
 *
 * @author Edward Robinson
 */
public class HomeMessagesAdapter extends RecyclerView.Adapter<HomeMessagesAdapter.ViewHolder> {

    /**
     * The list of people who the user is messaging with.
     */
    private List<MessageModel> mUserList;

    /** The adapter click listener. */
    private OnItemClickListener mListener;

    /**
     * Constructor for the message adapter.
     * @param theUserList The list of people who the user is messaging with
     */
    public HomeMessagesAdapter(List<MessageModel> theUserList) {
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
    public HomeMessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_messages_item_design, parent, false);
        return new ViewHolder(view, mListener);
    }

    /**
     * Binds the data from the message activity to inside our RecyclerView layout.
     *
     * @param holder The view holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull HomeMessagesAdapter.ViewHolder holder, int position) {
        Field[] drawablesFields = R.drawable.class.getFields();
        ArrayList<Drawable> drawables = new ArrayList<>();
        ImageView picture = holder.mPicture;

        for (Field field : drawablesFields) {
            try {
                if(field.getName().startsWith("shiba")) {
                    Log.i("LOG_TAG", "com.your.project.R.drawable." + field.getName());
                    drawables.add(picture.getResources().getDrawable(field.getInt(null)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Random rand = new Random();
        Drawable a = drawables.get(rand.nextInt(drawables.size()));

        String displayName = mUserList.get(position).getRecentName();
        String messageTime = mUserList.get(position).getMessageTime();
        String messageContent = mUserList.get(position).getMessageContent();

        holder.setData(a, displayName, messageTime, messageContent);
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** Profile picture. */
        private ImageView mPicture;

        /** Display name. */
        private TextView mDisplayName;

        /** Message time. */
        private TextView mMessageTime;

        /** Message content. */
        private TextView mMessageContent;

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
        public void setData(Drawable thePicture, String theDisplayName,
                            String theMessageTime, String theMessageContent) {
            mPicture.setImageDrawable(thePicture);
            mDisplayName.setText(theDisplayName);
            mMessageTime.setText(theMessageTime);
            mMessageContent.setText(theMessageContent);
        }
    }
}