package edu.uw.tcss450.howlr.ui.messages;

/**
 * The model of each message card in the messages page.
 * @author Daniel Jiang
 */
public class MessageModel {

    /** Profile picture. */
    private int mPicture;

    /** Display name. */
    private String mDisplayName;

    /** Message time. */
    private String mMessageTime;

    /** Message content. */
    private String mMessageContent;

    /**
     * Constructor for the message card in the messages page.
     * @param thePicture The profile picture
     * @param theDisplayName The display name
     * @param theMessageTime The message time
     * @param theMessageContent The message content
     */
    public MessageModel(int thePicture, String theDisplayName,
                        String theMessageTime, String theMessageContent) {
        this.mPicture = thePicture;
        this.mDisplayName = theDisplayName;
        this.mMessageTime = theMessageTime;
        this.mMessageContent = theMessageContent;
    }

    /**
     * Getter for the profile picture.
     * @return The profile picture
     */
    public int getPicture() {
        return mPicture;
    }

    /**
     * Getter for the display name.
     * @return The display name
     */
    public String getDisplayName() {
        return mDisplayName;
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
}