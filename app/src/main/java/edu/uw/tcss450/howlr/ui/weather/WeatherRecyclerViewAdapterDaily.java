package edu.uw.tcss450.howlr.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherDailyBinding;

/**
 * Implements WeatherRecyclerViewAdapterDaily to generate daily weather.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherRecyclerViewAdapterDaily extends RecyclerView.Adapter<WeatherRecyclerViewAdapterDaily.WeatherViewHolder> {
        private final List<Weather> mWeather;

        private boolean singleMode;

    public WeatherRecyclerViewAdapterDaily(List<Weather> mWeather) {
            this.mWeather = mWeather;
        }

    @NonNull
    @Override
    public WeatherRecyclerViewAdapterDaily.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherRecyclerViewAdapterDaily.WeatherViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_daily,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerViewAdapterDaily.WeatherViewHolder holder, int position) {
        holder.setWeather(mWeather.get(position));
    }

    @Override
    public int getItemCount() {
        if(!singleMode) {
            return mWeather.size();
        } else {
            return 1;
        }
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentWeatherDailyBinding binding;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentWeatherDailyBinding.bind(itemView);
        }

        public void setWeather(final Weather weather) {
            binding.textWeatherDay.setText(String.valueOf(weather.getDate()));
            binding.textWeatherDailyLowTemp.setText(Math.round(Float.parseFloat(String.valueOf(weather.getLowTemp()))) + "°");
            binding.textWeatherDailyHighTemp.setText(Math.round(Float.parseFloat(String.valueOf(weather.getHighTemp()))) + "°") ;
            binding.textWeatherDailyHumidity.setText(String.valueOf(weather.getHumidity()) + "%");
            Picasso.get().load("https://openweathermap.org/img/wn/" + weather.getIcon() + "@2x.png").into(binding.imageWeatherviewDayConditon);

        }

    }
}
