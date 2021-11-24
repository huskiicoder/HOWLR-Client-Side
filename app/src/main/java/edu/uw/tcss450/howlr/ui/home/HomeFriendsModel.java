package edu.uw.tcss450.howlr.ui.home;

/**
 * The model of each message card in the messages page.
 * @author Daniel Jiang
 */
public class HomeFriendsModel {

    /** Profile picture. */
    private int mPicture;

    /** Display name. */
    private String mDisplayName;

    /**
     * Constructor for the message card in the messages page.
     * @param thePicture The profile picture
     * @param theDisplayName The display name
     */
    public HomeFriendsModel(int thePicture, String theDisplayName) {
        this.mPicture = thePicture;
        this.mDisplayName = theDisplayName;
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
    public String getDisplayName() { return mDisplayName; }
}