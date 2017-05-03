package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by swati on 02/05/2017.
 */

public class DetailActivity extends AppCompatActivity{

    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private TextView mDetailWeatherTextView;

    private String mForecast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailWeatherTextView = (TextView) findViewById(R.id.tv_detail_weather);

         mForecast = getIntent().getExtras().getString("WEATHER_KEY");

        Log.d("DetailActivity",mForecast);

        mDetailWeatherTextView.setText(mForecast);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }
}
