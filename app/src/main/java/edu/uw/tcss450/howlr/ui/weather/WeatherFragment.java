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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.howlr.MainActivity;
import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentSignInBinding;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * @author Edward Robinson, Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherFragment extends Fragment {
    private UserInfoViewModel mUserModel;
    private WeatherViewModel mWeatherModel;
    private LocationViewModel mModel;
    private FragmentWeatherBinding binding;
    private static boolean mDefault = true;
    /**
     * Blank Constructor
     */
    public WeatherFragment() {
        // Required empty public constructor
    }
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mDefault){
            mWeatherModel.connectGet(Double.toString(mModel.getCurrentLocation().getLatitude()),
                    Double.toString(mModel.getCurrentLocation().getLongitude()), mUserModel.getmJwt());
            mDefault = false;
        }
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

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
            mWeatherModel.connectGet(args.getLat(),args.getLng(), mUserModel.getmJwt());
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
                binding.textViewWeatherCondition.setText(String.valueOf(list.get(0).getCurentWeather()));
                binding.textViewHumidity.setText("Hunidity " + String.valueOf(list.get(0).getHumidity()) + "%");
                Picasso.get().load("https://openweathermap.org/img/wn/"+ "04d" + "@2x.png").into(binding.imageView);

                hourly_rv.setAdapter(new WeatherRecyclerViewAdapterHourly(hourly_list));
                daily_rv.setAdapter(new WeatherRecyclerViewAdapterDaily(daily_list));
            }
        });

    }

    public String getAddress(Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

    private void searchZipcode(View view) {
        EditText text = (EditText) getView().findViewById(R.id.textView_zipcode_search);
        String value = text.getText().toString();
        mWeatherModel.connectZipGet(value, mUserModel.getmJwt());
    }
}