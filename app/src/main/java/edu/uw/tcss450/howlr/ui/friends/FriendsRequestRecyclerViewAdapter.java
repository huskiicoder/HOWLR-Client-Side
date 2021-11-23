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

public class FriendsRequestRecyclerViewAdapter
        extends RecyclerView.Adapter<FriendsRequestRecyclerViewAdapter.FriendsRequestViewHolder> {
    private final FriendsRequestFragment mParent;

    List<Friends> mFriendsRequests;

    private Context context;


    public FriendsRequestRecyclerViewAdapter(List<Friends> requests, FriendsRequestFragment parent) {
        this.mFriendsRequests = requests;
        this.mParent = parent;
        context = parent.getContext();
    }

    @NonNull
    @Override
    public FriendsRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendsRequestViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_friends_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsRequestViewHolder holder, int position) {
        holder.setRequest(mFriendsRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return mFriendsRequests.size();
    }

    public class FriendsRequestViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public FragmentFriendsRequestCardBinding binding;

        public FriendsRequestViewHolder(View view) {
            super(view);
            mView = view;

            binding = FragmentFriendsRequestCardBinding.bind(view);


        }

        void setRequest(final Friends friends) {
            binding.textviewFirstName.setText(friends.getUserName());
            String fullName = friends.getFirstName() + " " + friends.getLastName();
            binding.textviewLastName.setText(fullName);
            binding.buttonAcceptRequest.setOnClickListener(view -> acceptRequest(this, friends));
            binding.buttonDeclineRequest.setOnClickListener(view -> declineRequest(this, friends));
        }

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

        private void declineRequest(final FriendsRequestViewHolder view, Friends friends) {
            mFriendsRequests.remove(friends);
            notifyItemRemoved(view.getLayoutPosition());
            final int memberId = friends.getMemberId();
            mParent.deleteContact(memberId);

            CharSequence text = "You've Declined A Friends Request!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
