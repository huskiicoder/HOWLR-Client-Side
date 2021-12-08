package edu.uw.tcss450.howlr.ui.weather;

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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
        mUserModel = provider.get(UserInfoViewModel.class);
        mWeatherModel = provider.get(WeatherViewModel.class);
        mModel = provider.get(LocationViewModel.class);
//        mWeatherModel.connectGet(mModel.getCurrentLocation().getLatitude(), mModel.getCurrentLocation().getLongitude(), mUserModel.getmJwt());
        mWeatherModel.connectGet(47,-122,mUserModel.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

//        String location = getAddress();

        final RecyclerView hourly_rv = binding.weatherRecyclerviewHourly;
        final RecyclerView daily_rv = binding.weatherRecyclerview10days;
        hourly_rv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false));
        daily_rv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false));
        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), list->{
            if (!list.isEmpty()){
                List<Weather> hourly_list = list.subList(1,25);
                List<Weather> daily_list = list.subList(26,33);
                binding.textCurrentTemp.setText(Math.round(Float.parseFloat(String.valueOf(list.get(0).getCurrentTemp()))) + "°");
//                binding.textViewLocation.setText(String.valueOf(list.get(0).getCity()));
                binding.textViewLocation.setText("Tacoma");
                binding.textViewWeatherCondition.setText(String.valueOf(list.get(0).getCurentWeather()));
                binding.textViewHumidity.setText("Hunidity " + String.valueOf(list.get(0).getHumidity()) + "%");
                Picasso.get().load("https://openweathermap.org/img/wn/"+ "04d" + "@2x.png").into(binding.imageView);

                hourly_rv.setAdapter(new WeatherRecyclerViewAdapterHourly(hourly_list));
                daily_rv.setAdapter(new WeatherRecyclerViewAdapterDaily(daily_list));
                System.out.println(list.size());
            }
        });
        binding.buttonMap.setOnClickListener(button ->
                Navigation.findNavController(getView())
                        .navigate(WeatherFragmentDirections.actionNavigationWeatherToNavigationMap()));
    }

    public String getAddress() {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> results = null;
        String address = "";
        try {
            results = geocoder.getFromLocation(mModel.getCurrentLocation().getLatitude(), mModel.getCurrentLocation().getLongitude(), 1);
            String cityName = results.get(0).getLocality();
            String stateName = results.get(0).getAdminArea();
            address += cityName + ", " + stateName;
        } catch (IOException e) {
            // nothing
        }
        return address;
    }
}