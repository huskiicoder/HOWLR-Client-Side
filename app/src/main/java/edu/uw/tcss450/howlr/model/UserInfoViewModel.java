package edu.uw.tcss450.howlr.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * The view model class for the registered user's information.
 */
public class UserInfoViewModel extends ViewModel {

    /**
     * The user's email.
     */
    private final String mEmail;

    /**
     * The user's member ID.
     */
    private int mMemberId;

    /**
     * The user's JSON web token.
     */
    private final String mJwt;

    /**
     * The chatroom ID.
     */
    private int chatRoom;

    /**
     * Creates a ViewModel for the user with their information.
     * @param email The user's email
     * @param memberId The user's member ID
     * @param jwt The user's JSON web token
     */
    private UserInfoViewModel(String email, int memberId, String jwt) {
        mEmail = email;
        mMemberId = memberId;
        mJwt = jwt;
    }

    /**
     * Gets the user's email.
     * @return The user's email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Gets the user's JSON web token.
     * @return The user's JSON web token
     */
    public String getJwt() {
        return mJwt;
    }

    /**
     * Gets the user's member ID.
     * @return The user's member ID
     */
    public int getMemberId() {
        return mMemberId;
    }

    /**
     * Sets the chatroom.
     * @param newRoom The new chatroom ID.
     */
    public void setChatRoom(int newRoom) { chatRoom = newRoom; }

    /**
     * Gets the chatroom ID.
     * @return The chatroom ID
     */
    public int getChatRoom() { return chatRoom; }

    /**
     * The factory class for the ViewModel of the user.
     */
    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        /**
         * The user's email.
         */
        private final String email;

        /**
         * The user's member ID.
         */
        private final int memberId;

        /**
         * The user's JSON web token.
         */
        private final String jwt;

        /**
         * Creates a ViewModel for the user using a factory.
         * @param email The user's email
         * @param memberId The user's member ID
         * @param jwt The user's JSON web token
         */
        public UserInfoViewModelFactory(String email, int memberId, String jwt) {
            this.memberId = memberId;
            this.email = email;
            this.jwt = jwt;
        }

        /**
         * Creates a ViewModel of type T using a model class.
         * @param modelClass The model class
         * @param <T> The type T
         * @return The ViewModel of type T
         */
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