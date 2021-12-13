package edu.uw.tcss450.howlr.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherDailyBinding;

/**
 * Implements RecyclerViewAdapter to generate daily weather.
 * @author Natalie Nguyen Hong
 * @version TCSS 450 Fall 2021
 */
public class WeatherRecyclerViewAdapterDaily extends RecyclerView.Adapter<WeatherRecyclerViewAdapterDaily.WeatherViewHolder> {

    /**
     * The list of weather objects.
     */
    private final List<Weather> mWeather;

    /**
     * The mode.
     */
    private boolean singleMode;

    /**
     * Creates a RecyclerViewAdapter to generate daily weather.
     * @param mWeather The list of weather objects
     */
    public WeatherRecyclerViewAdapterDaily(List<Weather> mWeather) {
        this.mWeather = mWeather;
    }

    /**
     * Creates the holder for the weather fragment view.
     * @param parent The parent fragment
     * @param viewType The View type
     * @return The View holder
     */
    @NonNull
    @Override
    public WeatherRecyclerViewAdapterDaily.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherRecyclerViewAdapterDaily.WeatherViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weather_daily,parent,false));
    }

    /**
     * The binding on the View holder.
     * @param holder The holder
     * @param position The position
     */
    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerViewAdapterDaily.WeatherViewHolder holder, int position) {
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
            return 1;
        }
    }

    /**
     * Class for creating a View holder for the weather.
     */
    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentWeatherDailyBinding binding;

        /**
         * Creates a View holder for the weather display.
         * @param itemView The View
         */
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentWeatherDailyBinding.bind(itemView);
        }

        /**
         * Sets the weather using a weather object.
         * @param weather A weather object
         */
        public void setWeather(final Weather weather) {
            binding.textWeatherDay.setText(String.valueOf(weather.getDate()));
            binding.textWeatherDailyLowTemp.setText(Math.round(Float.parseFloat(String.valueOf(weather.getLowTemp()))) + "°");
            binding.textWeatherDailyHighTemp.setText(Math.round(Float.parseFloat(String.valueOf(weather.getHighTemp()))) + "°") ;
            binding.textWeatherDailyHumidity.setText(String.valueOf(weather.getHumidity()) + "%");
//            Picasso.get().load("https://openweathermap.org/img/wn/" + weather.getIcon() + "@2x.png").into(binding.imageWeatherviewDayConditon);

            String a = "a" + weather.getIcon();
            Context context = binding.imageWeatherviewDayConditon.getContext();
            int id = context.getResources().getIdentifier(a, "drawable", context.getPackageName());
            binding.imageWeatherviewDayConditon.setImageResource(id);
        }
    }
}