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

/**
 * HomeWeatherAdapter for building the Home Weather RecyclerView Card
 *
 * @author Edward Robinson
 */
public class HomeWeatherAdapter extends RecyclerView.Adapter<HomeWeatherAdapter.ViewHolder> {
    /** List for each days weather */
    private List<Weather> mWeatherList;

    /** Not sure, gotta look */
    private boolean singleMode;

    /**
     * Public constructor that initializes the daily weather list.
     *
     * @param mWeatherList Daily weather list to be initialized.
     */
    public HomeWeatherAdapter(List<Weather> mWeatherList) {
        this.mWeatherList = mWeatherList;
        singleMode = false;
    }

    /**
     * Updates the weather RecyclerView.
     *
     * @param holder the ViewHolder
     * @param position the position
     */
    @Override
    public void onBindViewHolder(@NonNull HomeWeatherAdapter.ViewHolder holder, int position) {
        holder.setData(mWeatherList.get(position));
    }

    /**
     * Inflates the design of our item design.
     *
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

    /**
     * Getter for mWeatherList size
     *
     * @return mWeatherList size
     */
    @Override
    public int getItemCount() {

        if (!singleMode) {
            return mWeatherList.size();
        } else {
            return 6;
        }
    }

    /**
     * The ViewHolder for RecyclerView
     *
     * @author Edward Robinson
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
            /** The View for each item */
            private final View mView;

            /** Binding for the Home Weather card */
            private HomeWeatherItemDesignBinding binding;

            /**
             * The view holder constructor.
             *
             * @param itemView The item view
             */
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mView = itemView;
                binding = HomeWeatherItemDesignBinding.bind(itemView);
            }

            /**
             * Sets the data for the weather card.
             *
             * @param weather List of Weather objects that contain all info for current weather and
             *                the forecast
             */
            public void setData(final Weather weather) {
                binding.textWeatherForecastDayHome.setText(String.valueOf(weather.getDate()));
                binding.textWeatherForecastTempHome.setText(String.valueOf((int) weather.getLowTemp() +
                        "°/"  + String.valueOf((int) weather.getHighTemp() + "°")));
                String a = "a" + weather.getIcon();
                Context context = binding.imageWeatherForecastHome.getContext();
                int id = context.getResources().getIdentifier(a, "drawable", context.getPackageName());
                binding.imageWeatherForecastHome.setImageResource(id);
            }
        }
    }

