package edu.uw.tcss450.howlr.ui.messages.createChats;

import android.widget.ImageView;

import org.json.JSONObject;

import java.io.Serializable;

public class CreateChatFriendsModel implements Serializable {

    //    private int mPicture;
    private final int mMemberId;
    private final String mUserName;
    private final String mFirstName;
    private final String mLastName;
//    public Friends(int picture, int memberId, String userName, String firstName, String lastName) {
//        this.mPicture = picture;
//        this.mMemberId = memberId;
//        this.mUserName = userName;
//        this.mFirstName = firstName;
//        this.mLastName = lastName;
//    }

    public CreateChatFriendsModel(int memberId, String userName, String firstName, String lastName) {
        this.mMemberId = memberId;
        this.mUserName = userName;
        this.mFirstName = firstName;
        this.mLastName = lastName;
    }

    public CreateChatFriendsModel(JSONObject json) throws Exception {
        mMemberId = json.getInt("memberid");
        mUserName = json.getString("username");
        mFirstName = json.getString("firstname");
        mLastName = json.getString("lastname");
    }

//    public int getPicture() {
//        return mPicture;
//    }

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
