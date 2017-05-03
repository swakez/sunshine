package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by swati on 02/05/2017.
 */

public class DetailActivity extends AppCompatActivity{

    TextView detailWeatherTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailWeatherTextView = (TextView) findViewById(R.id.tv_detail_weather);

        String detailWeather = getIntent().getExtras().getString("WEATHER_KEY");

        Log.d("DetailActivity",detailWeather);

        detailWeatherTextView.setText(detailWeather);
    }
}
