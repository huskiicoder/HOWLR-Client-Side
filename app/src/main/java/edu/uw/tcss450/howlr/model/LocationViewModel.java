package edu.uw.tcss450.howlr.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * The view model class for locations for the weather page.
 */
public class LocationViewModel extends ViewModel {

    /**
     * The list of live data from the location.
     */
    private MutableLiveData<Location> mLocation;

    /**
     * Creates the list of live data from the location.
     */
    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
    }

    /**
     * Adds a observer that listens to the location.
     * @param owner The owner of the fragment lifecycle
     * @param observer The observer
     */
    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    /**
     * Sets the location of the view model to the new location.
     * @param location The new location
     */
    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    /**
     * Gets the current location.
     * @return The current location
     */
    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }
}