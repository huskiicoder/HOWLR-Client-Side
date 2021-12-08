package edu.uw.tcss450.howlr.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserInfoViewModel extends ViewModel {

    private final String mEmail;
    private final int mMemberId;
    private final String mJwt;
    private int chatRoom;

    private UserInfoViewModel(String email, int memberId, String jwt) {
        mEmail = email;
        mMemberId = memberId;
        mJwt = jwt;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getmJwt() {
        return mJwt;
    }

    public int getmMemberId() {
        return mMemberId;
    }

    public void setChatRoom(int newRoom) { chatRoom = newRoom; }

    public int getChatRoom() { return chatRoom; }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final int memberId;
        private final String jwt;

        public UserInfoViewModelFactory(String email, int memberId, String jwt) {
            this.memberId = memberId;
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, memberId, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }


}

