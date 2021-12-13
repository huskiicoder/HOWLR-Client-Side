package edu.uw.tcss450.howlr.ui.friends;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.uw.tcss450.howlr.R;

/**
 * Implements Friends with memberId, username, lastname, firstname.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class Friends implements Serializable {
    private int mPicture;
    /** Member id of this friend. */
    private final int mMemberId;

    /** Username of this friend. */
    private String mUserName;

    /** First name of this friend. */
    private final String mFirstName;

    /** Last name of this friend. */
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

    public void setmUserName(String userName) {
        this.mUserName = userName;
    }

    public static Friends createFromJsonString(final String json) throws JSONException {
        final JSONObject msg = new JSONObject(json);
        return new Friends(msg.getInt("memberid"),
                msg.getString("firstname"),
                msg.getString("lastname"),
                msg.getString("username"));
    }

    /**
     * Constructs a friend from JSONobject.
     * @param json a json object
     * @throws Exception
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
     * Get memberid of the friend.
     * @return memberid of the friend.
     */
    public int getMemberId()
    {
        return mMemberId;
    }

    /**
     * Get first name of the friend.
     * @return first name of the friend.
     */
    public String getFirstName()
    {
        return mFirstName;
    }

    /**
     * Get username of the friend.
     * @return username of the friend.
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Get last name of the friend.
     * @return last name of the friend.
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
