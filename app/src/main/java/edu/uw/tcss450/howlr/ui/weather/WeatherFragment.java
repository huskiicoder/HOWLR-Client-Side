package edu.uw.tcss450.howlr.ui.weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * Fragment for weather which displays current weather, 24 hour forecast,
 * and multi-day forecast for current location. Allows for location searching
 * via zip code, as well as via map.
 * @author Edward Robinson, Natalie Nguyen Hong, Amir Almemar
 * @version TCSS 450 Fall 2021
 */
public class WeatherFragment extends Fragment {

    /**
     * The ViewModel for the user's information.
     */
    private UserInfoViewModel mUserModel;

    /**
     * The ViewModel for the weather.
     */
    private WeatherViewModel mWeatherModel;

    /**
     * The ViewModel for the location.
     */
    private LocationViewModel mModel;

    /**
     * The binding for the weather fragment.
     */
    private FragmentWeatherBinding binding;

    /**
     * Default value.
     */
    private static boolean mDefault = true;

    /**
     * The geocoder.
     */
    private Geocoder mGeocoder;

    /**
     * Empty constructor for the weather fragment.
     */
    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * On the weather fragment's creation.
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mWeatherModel = provider.get(WeatherViewModel.class);
        mModel = provider.get(LocationViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            mWeatherModel.setUserInfoViewModel(activity.getUserInfoViewModel());
        }
        mGeocoder = new Geocoder(getActivity());
    }

    /**
     * Creates the weather fragment's view.
     * @param inflater The inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mWeatherModel.connectGet("47","-122", mUserModel.getJwt());
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    /**
     * On the weather fragment's view creation.
     * @param view The View
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        binding.buttonMap.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(WeatherFragmentDirections.actionNavigationWeatherToNavigationMap()));
        binding.buttonSearch.setOnClickListener(this::searchZipcode);

        WeatherFragmentArgs args = WeatherFragmentArgs.fromBundle(getArguments());
        if (!args.getLat().equals("default") && !args.getLng().equals("default")){
            mWeatherModel.connectGet(args.getLat(),args.getLng(), mUserModel.getJwt());
            String address = getAddress(Double.parseDouble(args.getLat()), Double.parseDouble(args.getLng()));
            binding.textViewLocation.setText(address);
        }

        mWeatherModel.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (!location.isEmpty()) {
                String address = getAddress(location.get("lat"), location.get("lon"));
                binding.textViewLocation.setText(address);
            }
        });

        final RecyclerView hourly_rv = binding.weatherRecyclerviewHourly;
        final RecyclerView daily_rv = binding.weatherRecyclerview10days;
        hourly_rv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false));
        daily_rv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false));
        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), list->{
            if (!list.isEmpty()){
                List<Weather> hourly_list = list.subList(1,25);
                List<Weather> daily_list = list.subList(26,33);
                binding.textCurrentTemp.setText(Math.round(Float.parseFloat(String.valueOf(list.get(0).getCurrentTemp()))) + "°");
                binding.textViewWeatherCondition.setText(String.valueOf(list.get(0).getCurrentWeather()));
                binding.textViewHumidity.setText("Humidity " + String.valueOf(list.get(0).getHumidity()) + "%");

                String a = "a" + list.get(0).getIcon();
                Context context = binding.imageView.getContext();
                int id = context.getResources().getIdentifier(a, "drawable", context.getPackageName());
                binding.imageView.setImageResource(id);

                hourly_rv.setAdapter(new WeatherRecyclerViewAdapterHourly(hourly_list));
                daily_rv.setAdapter(new WeatherRecyclerViewAdapterDaily(daily_list));
            }
        });

    }

    /**
     * Gets the address from the latitude and longitude coordinates.
     * @param lat The latitude coordinates
     * @param lon The longitude coordinates
     * @return The address
     */
    public String getAddress(Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> results = null;
        String address = "";
        try {
            results = geocoder.getFromLocation(lat, lon, 1);
            String cityName = results.get(0).getLocality();
            String stateName = results.get(0).getAdminArea();
            address += cityName + ", " + stateName;
        } catch (IOException e) {
            // nothing
        }
        return address;
    }

    /**
     * Searches the location using the zipcode.
     * @param view The View
     */
    private void searchZipcode(View view) {
        EditText text = getView().findViewById(R.id.textView_zipcode_search);

        String location = text.getText().toString();
        Log.e("Location", location);
        List<Address> addressList = new ArrayList<>();
        String mCity = "Unknown";
        if (location != null || !location.equals("")){
            try {
                addressList = mGeocoder.getFromLocationName(location,1);
                Log.e("Add", String.valueOf(addressList.size()));
            } catch (IOException e){
                e.printStackTrace();
            }
            if (addressList.size() == 0){
                //Send an error message
                Log.e("Geocoder", "There is no location");
            }
            else {
                Address address = addressList.get(0);
                //get lat long coordinates
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mWeatherModel.connectGet(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude),mUserModel.getJwt());
                if (addressList.get(0).getLocality() != null){
                    mCity = addressList.get(0).getLocality();
                } else {
                    mCity = "Unknown";
                }
            }
        }
    }
}