package edu.uw.tcss450.authentication.ui.friends;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Friends implements Serializable {
    private final int mMemberId;
    private final String mUserName;
    private final String mFirstName;
    private final String mLastName;
    public Friends(int memberId, String userName, String firstName, String lastName) {
        this.mMemberId = memberId;
        this.mUserName = userName;
        this.mFirstName = firstName;
        this.mLastName = lastName;
    }

    public Friends(JSONObject json) throws Exception {
        mMemberId = json.getInt("memberid");
        mUserName = json.getString("username");
        mFirstName = json.getString("firstname");
        mLastName = json.getString("lastname");
    }
    public int getMemberId()
    {
        return mMemberId;
    }
    public String getFirstName()
    {
        return mFirstName;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getLastName() {
        return mLastName;
    }

}
