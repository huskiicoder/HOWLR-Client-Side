package edu.uw.tcss450.howlr.ui.weather;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentMapsBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;

/**
 * Class for the location fragment for the Google map inside of the weather fragment.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    /**
     * The Google map.
     */
    private GoogleMap mMap;

    /**
     * The ViewModel for the location fragment.
     */
    private LocationViewModel mModel;

    /**
     * The latitude/longitude.
     */
    private LatLng mLatLng;

    /**
     * Empty constructor for the location fragment.
     */
    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the location fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    /**
     * On the location fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMapsBinding binding = FragmentMapsBinding.bind(getView());

        mModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mModel.addLocationObserver(getViewLifecycleOwner(), location ->
                binding.textLatLong.setText(location.toString()));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync(this);
        binding.buttonSearch.setOnClickListener(this::setLocation);
    }

    /**
     * Sets the location using the view.
     * @param view The View
     */
    private void setLocation(View view) {
        LocationFragmentDirections.ActionNavigationMapToNavigationWeather location =
                LocationFragmentDirections.actionNavigationMapToNavigationWeather();
        location.setLat(Double.toString(mLatLng.latitude));
        location.setLng(Double.toString(mLatLng.longitude));
        Navigation.findNavController(getView()).navigate(location);
    }

    /**
     * When the map is ready to display.
     * @param googleMap The Google map
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FragmentMapsBinding binding = FragmentMapsBinding.bind(getView());
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if(location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);

                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                mLatLng = c;
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
                binding.textLatLong.setText("Latitude:" + Double.toString(c.latitude) + "\nLongitude:" + Double.toString(c.longitude));
            }
        });
        mMap.setOnMapClickListener(this);
    }

    /**
     * On map click action.
     * @param latLng The latitude/longitude of the location clicked
     */
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
        FragmentMapsBinding binding = FragmentMapsBinding.bind(getView());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));
        binding.textLatLong.setText("Latitude:" + Double.toString(latLng.latitude) + "\nLongitude:" + Double.toString(latLng.longitude));
        mLatLng = latLng;
    }
}