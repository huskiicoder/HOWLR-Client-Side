package edu.uw.tcss450.howlr.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Implement a viewmodel when a new friend is added or request.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class NewFriendCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewFriendCount;

    /**
     * Constructs a NewFriendCountViewModel.
     */
    public NewFriendCountViewModel() {
        mNewFriendCount = new MutableLiveData<>();
        mNewFriendCount.setValue(0);
    }

    /**
     * Implements a method to add new friend observer.
     * @param owner
     * @param observer
     */
    public void addFriendCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewFriendCount.observe(owner, observer);
    }

    /**
     * Implements a method to increase the number of new friends.
     */
    public void increment() {
        mNewFriendCount.setValue(mNewFriendCount.getValue() + 1);
    }

    /**
     * Resets the number of new friend requested.
     */
    public void reset() {
        mNewFriendCount.setValue(0);
    }
}
