package edu.uw.tcss450.howlr.ui.home;

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
public class HomeFriendsAdapter extends RecyclerView.Adapter<HomeFriendsAdapter.ViewHolder> {

    /**
     * The list of people who the user is messaging with.
     */
    private List<HomeFriendsModel> mUserList;

    /**
     * Constructor for the message adapter.
     * @param theUserList The list of people who the user is messaging with
     */
    public HomeFriendsAdapter(List<HomeFriendsModel> theUserList) {
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
    public HomeFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_friends_item_design, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data from the message activity to inside our RecyclerView layout.
     * @param holder The view holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull HomeFriendsAdapter.ViewHolder holder, int position) {
        int picture = mUserList.get(position).getPicture();
        String displayName = mUserList.get(position).getDisplayName();

        holder.setData(picture, displayName);
    }

    /**
     * The list size.
     * @return The list size
     */
    @Override
    public int getItemCount() {
        return mUserList.size();
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPicture = itemView.findViewById(R.id.picture);
            mDisplayName = itemView.findViewById(R.id.display_name);
        }

        /**
         * Sets the data for the message card.
         * @param thePicture The profile picture
         * @param theDisplayName The display name
         */
        public void setData(int thePicture, String theDisplayName) {
            mPicture.setImageResource(thePicture);
            mDisplayName.setText(theDisplayName);
        }
    }
}