package edu.uw.tcss450.howlr.ui.messages.chats;

import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentChatMessageBinding;

/**
 * The adapter for the chat fragment's recycler view.
 */
public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MessageViewHolder> {

    /** The list of chat messages to be binded to the adapter. */
    private final List<ChatMessage> mMessages;

    /** The email of the sender. */
    private final String mEmail;

    /**
     * Instantiates the recycler view adapter.
     * @param messages The new list of chat messages.
     * @param email The new email.
     */
    public ChatRecyclerViewAdapter(List<ChatMessage> messages, String email) {
        this.mMessages = messages;
        this.mEmail = email;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_message, parent, false));
    }

    /**
     * Binds the contents to the holder.
     * @param holder The holder to be binded to.
     * @param position The position of the holder.
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * The view holder of the adapter.
     */
    class MessageViewHolder extends RecyclerView.ViewHolder {

        /** The view. */
        private final View mView;

        /** The fragment binding. */
        private final FragmentChatMessageBinding binding;

        /**
         * Instantiates the view holder.
         * @param view The view
         */
        public MessageViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            this.binding = FragmentChatMessageBinding.bind(view);
        }

        /**
         * Sets the message to the view holder.
         * @param message The chat message.
         */
        void setMessage(final ChatMessage message) {
            final Resources res = mView.getContext().getResources();
            final MaterialCardView card = binding.cardRoot;

            int standard = (int) res.getDimension(R.dimen.chat_margin);
            int extended = (int) res.getDimension(R.dimen.chat_margin_sided);

            if (mEmail.equals(message.getEmail())) {
                //This message is from the user.
                binding.textMessage.setText(message.getMessage());
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                layoutParams.setMargins(extended, standard, standard, standard);
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
                        Gravity.END;

                card.setCardBackgroundColor(
                        ColorUtils.setAlphaComponent(
                                res.getColor(R.color.primaryLightColor, null),
                                16));
                binding.textMessage.setTextColor(
                        res.getColor(R.color.secondaryTextColorFade, null));

                card.setStrokeWidth(standard / 5);
                card.setStrokeColor(ColorUtils.setAlphaComponent(
                        res.getColor(R.color.primaryLightColor, null),
                        200));

                //Round the corners on the left side
                card.setShapeAppearanceModel(
                        card.getShapeAppearanceModel()
                                .toBuilder()
                                .setTopLeftCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomLeftCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomRightCorner(CornerFamily.ROUNDED, standard * 2)
                                .setTopRightCornerSize(0)
                                .build());

                card.requestLayout();
            } else {
                //This message is from another user.
                binding.textMessage.setText(message.getSender() +
                        ": " + message.getMessage());
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) card.getLayoutParams();

                layoutParams.setMargins(standard, standard, extended, standard);
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
                        Gravity.START;

                card.setCardBackgroundColor(
                        ColorUtils.setAlphaComponent(
                                res.getColor(R.color.secondaryLightColor, null),
                                16));

                card.setStrokeWidth(standard / 5);
                card.setStrokeColor(ColorUtils.setAlphaComponent(
                        res.getColor(R.color.secondaryLightColor, null),
                        200));

                binding.textMessage.setTextColor(
                        res.getColor(R.color.secondaryTextColorFade, null));

                //Round the corners on the right side
                card.setShapeAppearanceModel(
                        card.getShapeAppearanceModel()
                                .toBuilder()
                                .setTopRightCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomRightCorner(CornerFamily.ROUNDED,standard * 2)
                                .setBottomLeftCorner(CornerFamily.ROUNDED, standard * 2)
                                .setTopLeftCornerSize(0)
                                .build());
                card.requestLayout();
            }
        }
    }
}
