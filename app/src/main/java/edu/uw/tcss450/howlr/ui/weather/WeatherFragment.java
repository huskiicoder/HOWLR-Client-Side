package edu.uw.tcss450.howlr.ui.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentHomeBinding;
import edu.uw.tcss450.howlr.databinding.FragmentSignInBinding;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.howlr.model.LocationViewModel;
import edu.uw.tcss450.howlr.model.UserInfoViewModel;
import edu.uw.tcss450.howlr.ui.auth.signin.SignInFragmentDirections;
import edu.uw.tcss450.howlr.ui.weather.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private UserInfoViewModel mUserModel;
    private WeatherViewModel mWeatherModel;
    private FragmentWeatherBinding mBinding;
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
//        mWeatherModel = provider.get(WeatherViewModel.class);
//        mWeatherModel.connectGet(mUserModel.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWeatherBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());
//
//
//        final RecyclerView hourly_rv = binding.weatherRecyclerviewHourly;
//        final RecyclerView daily_rv = binding.weatherRecyclerview10days;
//        hourly_rv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false));
//        daily_rv.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false));
//        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), list->{
//            if (!list.isEmpty()){
//                List<Weather> hourly_list = list.subList(1,25);
//                List<Weather> daily_list = list.subList(26,33);
//                binding.textCurrentTemp.setText(String.valueOf(list.get(0).getCurrentTemp()) + "°");
//                binding.textViewLocation.setText(String.valueOf(list.get(0).getCity()));
//                binding.textViewWeatherCondition.setText(String.valueOf(list.get(0).getCurentWeather()));
//                Picasso.get().load("https://openweathermap.org/img/wn/"+ list.get(0).getIcon()+ "@2x.png").into(binding.imageView);
//                Picasso.get().load("https://openweathermap.org/img/wn/"+ "04d" + "@2x.png").into(binding.imageView);
//
//                hourly_rv.setAdapter(new WeatherRecyclerViewAdapterHourly(hourly_list));
//                daily_rv.setAdapter(new WeatherRecyclerViewAdapterDaily(daily_list));
//                System.out.println(list.size());
//            }
//        });
    }


}