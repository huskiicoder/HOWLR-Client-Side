package edu.uw.tcss450.authentication.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class NewFriendsCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewFriendCount;

    public NewFriendsCountViewModel() {
        mNewFriendCount = new MutableLiveData<>();
        mNewFriendCount.setValue(0);
    }

    public void addFriendsCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewFriendCount.observe(owner, observer);
    }

    public void increment() {
        mNewFriendCount.setValue(mNewFriendCount.getValue() + 1);
    }

    public void reset() {
        mNewFriendCount.setValue(0);
    }
}
