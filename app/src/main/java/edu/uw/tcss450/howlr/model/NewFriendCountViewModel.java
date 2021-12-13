package edu.uw.tcss450.howlr.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Implements a ViewModel for when a new friend is added or requested.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class NewFriendCountViewModel extends ViewModel {

    /**
     * Live data for the number of new friends.
     */
    private MutableLiveData<Integer> mNewFriendCount;

    /**
     * Creates a ViewModel for the number of new friends,
     * initially set to 0.
     */
    public NewFriendCountViewModel() {
        mNewFriendCount = new MutableLiveData<>();
        mNewFriendCount.setValue(0);
    }

    /**
     * Adds an observer to the counter for the number of new friends.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addFriendCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewFriendCount.observe(owner, observer);
    }

    /**
     * Increments the counter for the number of new friends.
     */
    public void increment() {
        mNewFriendCount.setValue(mNewFriendCount.getValue() + 1);
    }

    /**
     * Resets the number of new friend requests to 0.
     */
    public void reset() {
        mNewFriendCount.setValue(0);
    }
}