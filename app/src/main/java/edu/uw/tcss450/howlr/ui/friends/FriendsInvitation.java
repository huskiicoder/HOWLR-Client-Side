package edu.uw.tcss450.howlr.ui.friends;

import androidx.annotation.Nullable;

/**
 * Implements FriendsInvitation to provide username, first name, last name of the sender
 * for friend invitations.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */

public class FriendsInvitation {
    /**
     * String of the sender's user name
     */
    private final String mSenderUsername;

    /**
     * String of the sender's first name
     */
    private final String mSenderFirstName;

    /**
     * String of the sender's last name
     */
    private final String mSenderLastName;

    /**
     * Construct a friend invitation with username, first name, last name of the sender.
     * @param senderUsername the username of the sender
     * @param senderFirstName the first name of the sender
     * @param senderLastName the last name of the sender
     */
    public FriendsInvitation(final String senderUsername, final String senderFirstName, final String senderLastName) {
        this.mSenderUsername = senderUsername;
        this.mSenderFirstName = senderFirstName;
        this.mSenderLastName = senderLastName;
    }

    /**
     *
     * @param username
     * @param firstName
     * @param lastName
     * @return
     */
    public static FriendsInvitation createFromString(final String username, final String firstName, final String lastName) {
        return new FriendsInvitation(username, firstName, lastName);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof FriendsInvitation) {
            result = mSenderUsername.equals(((FriendsInvitation) other).getmSenderUsername());
        }
        return result;
    }

    /**
     * Get the username of the sender
     * @return the username of the sender
     */
    public String getmSenderUsername() {
        return mSenderUsername;
    }

    /**
     * Get the first name of the sender
     * @return the first name of the sender
     */
    public String getmSenderFirstName() {
        return mSenderFirstName;
    }

    /**
     * Get the last name of the sender
     * @return the last name of the sender
     */
    public String getmSenderLastName() {
        return mSenderLastName;
    }
}
