package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by swati on 01/05/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;

    final private ForecastAdapterOnClickHandler mClickHandler;

    public ForecastAdapter(ForecastAdapterOnClickHandler forecastAdapterOnClickHandler) {
        mClickHandler = forecastAdapterOnClickHandler;
    }

    /* Click interface to be implemented by mainActivity */

    public interface ForecastAdapterOnClickHandler {
        void onClick(String s);
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        String currentWeatherData = mWeatherData[position];
        holder.mWeatherTextView.setText(currentWeatherData);
    }

    // Returns the number of data
    @Override
    public int getItemCount() {
        if (mWeatherData != null)
            return mWeatherData.length;
        return 0;
    }

    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    public class ForecastAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String weatherForDay = mWeatherData[clickedPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }
}
