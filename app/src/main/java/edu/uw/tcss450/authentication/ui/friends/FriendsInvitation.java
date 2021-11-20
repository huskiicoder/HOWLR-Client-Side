package edu.uw.tcss450.authentication.ui.friends;

import androidx.annotation.Nullable;

public class FriendsInvitation {
    /**
     * String of the sender's username
     */
    private final String mSenderUsername;

    public FriendsInvitation(final String senderUsername) {
        this.mSenderUsername = senderUsername;
    }

    public static FriendsInvitation createFromString(final String contactRequestUsername) {
        return new FriendsInvitation(contactRequestUsername);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof FriendsInvitation) {
            result = mSenderUsername.equals(((FriendsInvitation) other).getSenderUsername());
        }
        return result;
    }

    public String getSenderUsername() {
        return mSenderUsername;
    }
}
