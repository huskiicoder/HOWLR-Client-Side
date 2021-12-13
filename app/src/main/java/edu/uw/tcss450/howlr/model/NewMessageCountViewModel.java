package edu.uw.tcss450.howlr.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Implements a ViewModel for when a new message is received.
 */
public class NewMessageCountViewModel extends ViewModel {

    /**
     *  Live data for the number of new messages.
     */
    private MutableLiveData<Integer> mNewMessageCount;

    /**
     * Creates a ViewModel for the number of new messages,
     * initially set to 0.
     */
    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
    }

    /**
     * Adds an observer to the counter for the number of new messages.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     * Increments the counter for the number of new messages.
     */
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    /**
     * Resets the number of new messages to 0.
     */
    public void reset() {
        mNewMessageCount.setValue(0);
    }
}