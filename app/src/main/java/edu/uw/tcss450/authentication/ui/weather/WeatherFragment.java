package edu.uw.tcss450.authentication.ui.weather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import edu.uw.tcss450.authentication.R;
import edu.uw.tcss450.authentication.databinding.FragmentSignInBinding;
import edu.uw.tcss450.authentication.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.authentication.model.LocationViewModel;
import edu.uw.tcss450.authentication.ui.auth.signin.SignInFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private WeatherViewModel mViewModel;
    private @NonNull FragmentWeatherBinding binding;
    private LocationViewModel mLocModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mLocModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        mViewModel.connectGetCurrent();
        binding.buttonBrokePos.setOnClickListener(button -> binding.textWeather.setText(mViewModel.mResponse.getValue().toString()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}