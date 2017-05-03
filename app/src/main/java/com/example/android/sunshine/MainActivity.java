/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.ForecastAdapterOnClickHandler {

    private RecyclerView mRecyclerView;

    private ForecastAdapter mForecastAdapter;

    private TextView mErrorMessageTextView;

    private ProgressBar pb_loading_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);

        mErrorMessageTextView = (TextView) findViewById(R.id.error_message);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);

        mRecyclerView.setAdapter(mForecastAdapter);

        pb_loading_data = (ProgressBar) findViewById(R.id.pb_loading_data);

        loadWeatherData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_refresh) {
            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }
        if (itemId == R.id.action_open_map) {
            openLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLocationInMap(){

        String addressString = "1600 Ampitheatre Parkway, CA";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    public void loadWeatherData() {
        String preferredUserLocation = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(preferredUserLocation);
    }

    /**
     * This method is overridden by MainActivity.java in order to handle the clickevent on recycler view item
     *
     * @param weatherForDay the weather for the day that has been clicked
     */
    @Override
    public void onClick(String weatherForDay) {
        Context context = this;
        Class targetActivity = DetailActivity.class;

        Intent intent = new Intent(context, targetActivity);

        intent.putExtra("WEATHER_KEY", weatherForDay);

        startActivity(intent);

    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            pb_loading_data.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... param) {
            // If there is no zipcode, there is nothing to look up
            if (param == null) {
                return null;
            }
            String location = param[0];
            URL url = NetworkUtils.buildUrl(location);
            try {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(url);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                return simpleJsonWeatherData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] s) {
            pb_loading_data.setVisibility(View.INVISIBLE);
            if (s != null) {
                mForecastAdapter.setWeatherData(s);
                mRecyclerView.setVisibility(View.VISIBLE);
                mErrorMessageTextView.setVisibility(View.INVISIBLE);
            } else {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mErrorMessageTextView.setVisibility(View.VISIBLE);
            }

        }
    }
}