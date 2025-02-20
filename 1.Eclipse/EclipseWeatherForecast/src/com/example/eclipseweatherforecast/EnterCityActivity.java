package com.example.eclipseweatherforecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



public class EnterCityActivity extends Activity {
	
	TextView currTime;
    TextView currDay;
    TextView currDate;
    TextView currLoc;
    TextView currTemp;
    TextView humid;
    TextView feelsLikeTemp;
    TextView weatherDesc;
    ImageView weatherIcon;
    EditText edtCity;
    Button btnSub;
    ProgressBar pb;

    Thread th;
    private static final int REQUEST_LOCATION = 1;
   // protected LocationRequest mLocationRequest;
    protected long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    protected long FASTEST_INTERVAL = 2000; /* 2 sec */
    double latitude;
    double longitude;
    
    RequestQueue requestQueue;
    String url;
    private static final String KEY = "e25a68c009aa630161457691d6f0a5a6";
    private static final String DOUBLE_CURR_TEMP = "temp";
    private static final String DOUBLE_FEELS_LIKE = "feels_like";
    private static final String INT_HUMIDITY = "humidity";
    private static final String STRING_DESC = "description";
    private static final String INT_IMAGE_ID = "id";

    final String DEGREE = "\u00b0";
    final String CELSIUS = "C";
    final String PERCENTAGE = "%";
    final String FEELS_LIKE ="Feels Like ";
    final String HUMIDITY = "Humidity: ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_city);	
				
		currTime = (TextView) findViewById(R.id.tv_city_time);
        currDate = (TextView) findViewById(R.id.tv_city_date);
        currDay = (TextView) findViewById(R.id.tv_city_day);
        currLoc = (TextView) findViewById(R.id.tv_location_name);
        currTemp = (TextView) findViewById(R.id.tv_current_temp);
        humid = (TextView) findViewById(R.id.tv_humidity);
        feelsLikeTemp = (TextView) findViewById(R.id.tv_feels_like);
        weatherDesc = (TextView) findViewById(R.id.tv_description);
        weatherIcon = (ImageView) findViewById(R.id.iv_climate_icon);
        edtCity = (EditText) findViewById(R.id.et_city_name);
        btnSub = (Button) findViewById(R.id.btn_submit);
        pb = (ProgressBar) findViewById(R.id.progressBar1);

        pb.setVisibility(View.VISIBLE);
		
		btnSub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent i = new Intent(EnterCityActivity.this,
						DateListActivity.class);
				Bundle value = new Bundle();
				value.putString("citi", edtCity.getText().toString());
				i.putExtras(value); // intent is merged with bundle
				startActivity(i);

			}
		});
		
		th = new Thread() {
            @Override
            public void run() {
                try {
                    while (!th.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String currentTime = new SimpleDateFormat("hh : mm a",
                                        Locale.getDefault()).format(Calendar.getInstance().getTime());
                                String currentDate = new SimpleDateFormat("yyyy-MM-dd",
                                        Locale.getDefault()).format(new Date());
                                String currentDay = new SimpleDateFormat("EEEE",
                                        Locale.ENGLISH).format(Calendar.getInstance().getTime());

                                currTime.setText(currentTime);
                                currDate.setText(currentDate);
                                currDay.setText(currentDay);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
		
	}
	
	
}
