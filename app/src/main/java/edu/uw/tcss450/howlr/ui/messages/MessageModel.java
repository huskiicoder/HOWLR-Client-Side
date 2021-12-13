package edu.uw.tcss450.howlr.ui.messages;

import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * The model of each message card in the messages page.
 * @author Daniel Jiang
 */
public class MessageModel implements Serializable {

    /** Profile picture. */
    private final int mPicture;

    /** Chat ID */
    private final int mChatId;

    /** Recent Name. */
    private final String mRecentName;

    /** Message content. */
    private final String mMessageContent;

    /** Message time. */
    private final String mMessageTime;

    /**
     * Constructor for the message card in the messages page.
     * @param theChatId The chat ID
     * @param theRecentName The recent message name
     * @param theMessageTime The recent message time
     * @param theMessageContent The recent message content
     */
    public MessageModel(int thePicture, int theChatId, String theRecentName,
                        String theMessageTime, String theMessageContent) {
        this.mPicture = thePicture;
        this.mChatId = theChatId;
        this.mRecentName = theRecentName;
        this.mMessageTime = theMessageTime;
        this.mMessageContent = theMessageContent;
    }

    public int getPicture() { return mPicture; }

    /**
     * Getter for the chat ID.
     * @return The profile picture
     */
    public int getChatId() {
        return mChatId;
    }

    /**
     * Getter for the recent name.
     * @return The display name
     */
    public String getRecentName() {
        return mRecentName;
    }

    /**
     * Getter for the recent message time.
     * @return The message time
     */
    public String getMessageTime() {
        return mMessageTime;
    }

    /**
     * Getter for the recent message content.
     * @return The message content
     */
    public String getMessageContent() {
        return mMessageContent;
    }

    /**
     * Provides equality solely based on ChatId.
     * @param other the other object to check for equality
     * @return true if other chat ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof MessageModel) {
            result = mChatId == ((MessageModel) other).mChatId;
        }
        return result;
    }
}