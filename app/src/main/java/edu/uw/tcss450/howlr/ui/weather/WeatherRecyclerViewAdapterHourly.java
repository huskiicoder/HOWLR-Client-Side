package edu.uw.tcss450.howlr.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherHourlyBinding;

/**
 * Implements RecyclerViewAdapter to generate hourly weather.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherRecyclerViewAdapterHourly extends RecyclerView.Adapter<WeatherRecyclerViewAdapterHourly.WeatherViewHolder> {

    /**
     * The list of weather objects.
     */
    private final List<Weather> mWeather;

    /**
     * The mode.
     */
    private boolean singleMode;

    /**
     * Creates a RecyclerViewAdapter to generate hourly weather.
     * @param mWeather The list of weather objects
     */
    public WeatherRecyclerViewAdapterHourly(List<Weather> mWeather) {
        this.mWeather = mWeather;
        singleMode = false;
    }

    /**
     * Creates the holder for the weather fragment view.
     * @param parent The parent fragment
     * @param viewType The View type
     * @return The View holder
     */
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_weather_hourly,parent,false));
    }

    /**
     * The binding on the View holder.
     * @param holder The holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setWeather(mWeather.get(position));
    }

    /**
     * Returns the number of weather objects.
     * @return The number of weather objects
     */
    @Override
    public int getItemCount() {
        if(!singleMode) {
            return mWeather.size();
        } else {
            return 6;
        }
    }

    /**
     * Sets the mode to single day.
     */
    public void setToSingleDay() {
        singleMode = true;
    }

    /**
     * Class for creating a View holder for the weather.
     */
    public class WeatherViewHolder extends RecyclerView.ViewHolder{
        private final View mView;
        private FragmentWeatherHourlyBinding binding;

        /**
         * Creates a View holder for the weather display.
         * @param itemView The View
         */
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentWeatherHourlyBinding.bind(itemView);
        }

        /**
         * Sets the weather using a weather object.
         * @param weather A weather object
         */
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