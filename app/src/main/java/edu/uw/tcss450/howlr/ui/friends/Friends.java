package edu.uw.tcss450.howlr.ui.friends;

import org.json.JSONObject;

import java.io.Serializable;

import edu.uw.tcss450.howlr.R;

/**
 * Implements friends with their memberID, username, first name, and last name.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class Friends implements Serializable {

    /**
     * Profile picture of the friend.
     */
    private int mPicture;

    /**
     * The member ID of the friend.
     */
    private final int mMemberId;

    /**
     * The username of the friend.
     */
    private final String mUserName;

    /**
     * The first name of the friend.
     */
    private final String mFirstName;

    /**
     * The last name of the friend.
     */
    private final String mLastName;

    /**
     * Creates a friend with the friend's information.
     * @param memberId The friend's member ID
     * @param userName The friend's username
     * @param firstName The friend's first name
     * @param lastName The friend's last name
     */
    public Friends(int memberId, String userName, String firstName, String lastName) {
        this.mPicture = R.drawable.shibaheart;
        this.mMemberId = memberId;
        this.mUserName = userName;
        this.mFirstName = firstName;
        this.mLastName = lastName;
    }

//    /**
//     * Constructs a friend object.
//     * @param memberId member id of the friend.
//     * @param userName username of the friend.
//     * @param firstName first name of the friend.
//     * @param lastName last name of the friend.
//     */
//    public Friends(int memberId, String userName, String firstName, String lastName) {
//        this.mMemberId = memberId;
//        this.mUserName = userName;
//        this.mFirstName = firstName;
//        this.mLastName = lastName;
//    }

    /**
     * Constructs a friend from the JSON object.
     * @param json The JSON object
     * @throws Exception The exception
     */
    public Friends(JSONObject json) throws Exception {
        mMemberId = json.getInt("memberid");
        mUserName = json.getString("username");
        mFirstName = json.getString("firstname");
        mLastName = json.getString("lastname");
    }

    /**
     * Gets the friend's profile picture.
     * @return The friend's profile picture
     */
    public int getPicture() {
        return mPicture;
    }

    /**
     * Gets the friend's member ID.
     * @return The friend's member ID
     */
    public int getMemberId()
    {
        return mMemberId;
    }

    /**
     * Gets the friend's first name.
     * @return The friend's first name
     */
    public String getFirstName()
    {
        return mFirstName;
    }

    /**
     * Gets the friend's username.
     * @return The friend's username
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Gets the friend's first name.
     * @return The friend's first name
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Returns the String object of the Friends object.
     * @return The String object of the Friends object
     */
    @Override
    public String toString() {
        return "Friends{" +
                "mMemberId=" + mMemberId +
                ", mUserName='" + mUserName + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                '}';
    }
}