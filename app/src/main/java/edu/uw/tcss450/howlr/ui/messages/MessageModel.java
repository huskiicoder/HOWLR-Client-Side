package edu.uw.tcss450.howlr.ui.messages;

import androidx.annotation.Nullable;

import java.io.Serializable;

import edu.uw.tcss450.howlr.ui.messages.chats.ChatMessage;

/**
 * The model of each message card in the messages page.
 * @author Daniel Jiang
 */
public class MessageModel implements Serializable {

    /** Profile picture.
    private int mPicture; */

    /** Chat ID */
    private int mChatId;

    /** Recent Name. */
    private String mRecentName;

    /** Message content. */
    private String mMessageContent;

    /** Message time. */
    private String mMessageTime;

    /**
     * Constructor for the message card in the messages page.
     * @param theChatId The profile picture
     * @param theRecentName The display name
     * @param theMessageTime The message time
     * @param theMessageContent The message content
     */
    public MessageModel(int theChatId, String theRecentName,
                        String theMessageTime, String theMessageContent) {
        this.mChatId = theChatId;
        this.mRecentName = theRecentName;
        this.mMessageTime = theMessageTime;
        this.mMessageContent = theMessageContent;
    }

    /**
     * Getter for the profile picture.
     * @return The profile picture
     */
    public int getChatId() {
        return mChatId;
    }

    /**
     * Getter for the display name.
     * @return The display name
     */
    public String getRecentName() {
        return mRecentName;
    }

    /**
     * Getter for the message time.
     * @return The message time
     */
    public String getMessageTime() {
        return mMessageTime;
    }

    /**
     * Getter for the message content.
     * @return The message content
     */
    public String getMessageContent() {
        return mMessageContent;
    }

    /**
     * Provides equality solely based on MessageId.
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
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