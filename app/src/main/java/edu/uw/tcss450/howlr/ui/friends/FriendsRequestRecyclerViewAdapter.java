package edu.uw.tcss450.howlr.ui.friends;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentFriendsRequestCardBinding;

/**
 * Implements RecyclerViewAdapter for incoming friend requests.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class FriendsRequestRecyclerViewAdapter
        extends RecyclerView.Adapter<FriendsRequestRecyclerViewAdapter.FriendsRequestViewHolder> {

    /**
     * The friend request fragment.
     */
    private final FriendsRequestFragment mParent;

    /**
     * The list of incoming friend requests.
     */
    List<Friends> mFriendsRequests;

    /**
     * The context.
     */
    private Context context;

    /**
     * Creates a RecyclerViewAdapter for incoming friend requests.
     * @param requests The incoming friend request
     * @param parent The friend request fragment
     */
    public FriendsRequestRecyclerViewAdapter(List<Friends> requests, FriendsRequestFragment parent) {
        this.mFriendsRequests = requests;
        this.mParent = parent;
        context = parent.getContext();
    }

    /**
     * Creates the holder for the friend request fragment view.
     * @param parent The parent fragment
     * @param viewType The View type
     * @return The View holder
     */
    @NonNull
    @Override
    public FriendsRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsRequestViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_request_card, parent, false));
    }

    /**
     * The binding on the View holder.
     * @param holder The holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull FriendsRequestViewHolder holder, int position) {
        holder.setRequest(mFriendsRequests.get(position));
    }

    /**
     * Returns the number of incoming friend requests.
     * @return The number of incoming friend requests
     */
    @Override
    public int getItemCount() {
        return mFriendsRequests.size();
    }

    /**
     * Class for creating a View holder for incoming friend requests.
     */
    public class FriendsRequestViewHolder extends RecyclerView.ViewHolder {

        /**
         * The View.
         */
        public final View mView;

        /**
         * The binding for the friend request cards.
         */
        public FragmentFriendsRequestCardBinding binding;

        /**
         * Creates a View holder for the friend request card.
         * @param view The View
         */
        public FriendsRequestViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentFriendsRequestCardBinding.bind(view);
        }

        /**
         * Sets the incoming friend request.
         * @param friends The friend who sent the request
         */
        void setRequest(final Friends friends) {
            binding.textviewName.setText(friends.getFirstName() + " " + friends.getLastName());
            binding.textviewEmail.setText(friends.getUserName());
            binding.buttonAcceptRequest.setOnClickListener(view -> acceptRequest(this, friends));
            binding.buttonDeclineRequest.setOnClickListener(view -> declineRequest(this, friends));
        }

        /**
         * Accepts the incoming friend request.
         * @param view The View
         * @param friends The friend who sent the request
         */
        private void acceptRequest(final FriendsRequestViewHolder view, Friends friends) {
            mFriendsRequests.remove(friends);
            notifyItemRemoved(view.getLayoutPosition());
            final int memberId = friends.getMemberId();
            mParent.acceptContact(memberId);

            CharSequence text = "You've Accepted A Friend Request!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Navigation.findNavController(mView).navigate(FriendsRequestFragmentDirections
                    .actionNavigationFriendsRequestToNavigationFriendsList());
        }

        /**
         * Declines the incoming friend request.
         * @param view The View
         * @param friends The friend who sent the request
         */
        private void declineRequest(final FriendsRequestViewHolder view, Friends friends) {
            mFriendsRequests.remove(friends);
            notifyItemRemoved(view.getLayoutPosition());
            final int memberId = friends.getMemberId();
            mParent.declineContact(memberId);

            CharSequence text = "You've Declined A Friends Request!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}