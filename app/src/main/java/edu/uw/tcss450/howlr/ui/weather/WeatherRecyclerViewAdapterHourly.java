package edu.uw.tcss450.howlr.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherHourlyBinding;

/**
 * Implements WeatherRecyclerViewAdapterHourly to generate hourly weather.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherRecyclerViewAdapterHourly extends RecyclerView.Adapter<WeatherRecyclerViewAdapterHourly.WeatherViewHolder> {
    private final List<Weather> mWeather;
    private boolean singleMode;

    public WeatherRecyclerViewAdapterHourly(List<Weather> mWeather) {
        this.mWeather = mWeather;
        singleMode = false;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weather_hourly,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setWeather(mWeather.get(position));
    }

    @Override
    public int getItemCount() {

        if(!singleMode) {
            return mWeather.size();
        } else {
            return 6;
        }
    }

    public void setToSingleDay() {
        singleMode = true;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        private final View mView;
        private FragmentWeatherHourlyBinding binding;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentWeatherHourlyBinding.bind(itemView);
        }

        void setWeather(final Weather weather){
            binding.textWeatherTime.setText((String.valueOf(weather.getTime()) + ":00"));
            binding.textWeatherTemperature.setText(Math.round(Float.parseFloat(String.valueOf(weather.getCurrentTemp()))) + "°");
            binding.textWeatherHumidity.setText(String.valueOf(weather.getHumidity()) + "%");
//            Picasso.get().load("https://openweathermap.org/img/wn/"+ weather.getIcon()+ "@2x.png").into(binding.imageWeatherIcon);

            String a = "a" + weather.getIcon();
            Context context = binding.imageWeatherIcon.getContext();
            int id = context.getResources().getIdentifier(a, "drawable", context.getPackageName());
            binding.imageWeatherIcon.setImageResource(id);
        }
    }
}
