package edu.uw.tcss450.howlr.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tcss450.howlr.R;
import edu.uw.tcss450.howlr.databinding.FragmentHomeBinding;
import edu.uw.tcss450.howlr.databinding.FragmentWeatherHourlyBinding;
import edu.uw.tcss450.howlr.databinding.HomeWeatherItemDesignBinding;
import edu.uw.tcss450.howlr.ui.friends.HomeFriendsAdapter;
import edu.uw.tcss450.howlr.ui.messages.MessageAdapter;
import edu.uw.tcss450.howlr.ui.weather.Weather;
import edu.uw.tcss450.howlr.ui.weather.WeatherRecyclerViewAdapterHourly;

public class HomeWeatherAdapter extends RecyclerView.Adapter<HomeWeatherAdapter.ViewHolder> {
    private List<Weather> mWeatherList;
    private boolean singleMode;

    public HomeWeatherAdapter(List<Weather> mWeatherList) {
        this.mWeatherList = mWeatherList;
        singleMode = false;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeWeatherAdapter.ViewHolder holder, int position) {
//        String day = String.valueOf(mWeatherList.get(0).getDate());
//        String temperature = String.valueOf(mWeatherList.get(0).getCurrentTemp());
//        String icon = mWeatherList.get(0).getIcon();
//        holder.setData(icon, temperature, day);
        holder.setData(mWeatherList.get(position));
    }

    /**
     * Inflates the design of our item design.
     * @param parent The parent
     * @param viewType The view type
     * @return The view holder
     */
    @NonNull
    @Override
    public HomeWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeWeatherAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.home_weather_item_design, parent, false));
    }

    @Override
    public int getItemCount() {

        if (!singleMode) {
            return mWeatherList.size();
        } else {
            return 6;
        }
    }

    public void setToSingleDay() {
        singleMode = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

            /**
             * Day of the week.
             */
            private TextView mDay;

            /**
             * Temperature.
             */
            private TextView mTemperature;

            /**
             * Weather icon.
             */
            private ImageView mWeatherIcon;

            private final View mView;
            private HomeWeatherItemDesignBinding binding;

            /**
             * The view holder constructor.
             *
             * @param itemView The item view
             */
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
//                mDay = itemView.findViewById(R.id.text_weather_forecast_day_home);
//                mTemperature = itemView.findViewById(R.id.text_weather_forecast_temp_home);
//                mWeatherIcon = itemView.findViewById(R.id.image_weather_forecast_home);
                mView = itemView;
                binding = HomeWeatherItemDesignBinding.bind(itemView);
            }

            /**
             * Sets the data for the weather card.
             *
             * @param theWeatherIcon    The weather icon
             * @param theTemperature    The temperature
             * @param theDay            The day
             */
            public void setData(final Weather weather) {
                /*Picasso.get().load("https://openweathermap.org/img/wn/"+ theWeatherIcon + "@2x.png").into(mWeatherIcon);
                mTemperature.setText(theTemperature);
                mDay.setText(theDay);*/
                binding.textWeatherForecastDayHome.setText(String.valueOf(weather.getDate()));
                binding.textWeatherForecastTempHome.setText(String.valueOf((int) weather.getLowTemp() +
                        "°/"  + String.valueOf((int) weather.getHighTemp() + "°")));
                String a = "a" + weather.getIcon();
                Context context = binding.imageWeatherForecastHome.getContext();
                int id = context.getResources().getIdentifier(a, "drawable", context.getPackageName());
                binding.imageWeatherForecastHome.setImageResource(id);


//                Picasso.get().load("https://openweathermap.org/img/wn/" + weather.getIcon() +
//                        "@2x.png").into(binding.imageWeatherForecastHome);
            }
        }
    }

