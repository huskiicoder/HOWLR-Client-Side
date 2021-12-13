package edu.uw.tcss450.howlr.ui.friends;

import static edu.uw.tcss450.howlr.R.drawable.shibadab;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsCardBinding;
import edu.uw.tcss450.howlr.ui.home.HomeFragment;

/**
 * Adapted class used to build the Friends RecyclerView on Home page.
 *
 * @author Edward Robinson
 */
public class HomeFriendsAdapter extends
        RecyclerView.Adapter<HomeFriendsAdapter.ViewHolder> {

    /** Used to initialize parent Fragment */
    private final HomeFragment mParent;

    /** Builds the list of Friends to be initialized to the RecyclerView */
    List<Friends> mFriends;

    /**
     * Public constructor to initialize the Adapter for friends RecyclerView
     *
     * @param friends the list of friends to be added to the RecyclerView
     * @param parent the parent fragment
     */
    public HomeFriendsAdapter(List<Friends> friends, HomeFragment parent) {
        this.mFriends = friends;
        this.mParent = parent;
    }

    /**
     * Inflates the layout using ViewHolder
     *
     * @param parent the parent ViewGroup
     * @param viewType the view type
     * @return the ViewHolder and inflated layout
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.home_friends_item_design, parent, false));
    }

    /**
     * Updates RecyclerView
     * @param holder the ViewHolder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setContact(mFriends.get(position));
    }

    /**
     * Getter for the item count
     *
     * @return size of the friends list
     */
    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    /**
     * ViewHolder class.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /** Profile picture. */
        private ImageView mPicture;

        /** Display name. */
        private TextView mDisplayName;

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
         * Sets the data for the friends card.
         *
         * @param friends Friends object to be set
         */
        public void setContact(final Friends friends) {
            Field[] drawablesFields = R.drawable.class.getFields();
            ArrayList<Drawable> drawables = new ArrayList<>();

            for (Field field : drawablesFields) {
                try {
                    if(field.getName().startsWith("shiba")) {
                        Log.i("LOG_TAG", "com.your.project.R.drawable." + field.getName());
                        drawables.add(mPicture.getResources().getDrawable(field.getInt(null)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Random rand = new Random();
            Drawable a = drawables.get(rand.nextInt(drawables.size()));
            mPicture.setImageDrawable(a);
            mDisplayName.setText(friends.getFirstName() + " " + friends.getLastName());
        }
    }
}