package com.example.eclipseweatherforecast;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DateListActivity extends Activity {
	
	private static String url;
	private static final String KEY = "api--key";
	
	private static final String OBJ_DATA = "data";
	
	private static final String OBJ_ARR_CURR = "current_condition";
	
	private static final String INT_TEMP = "temp_C";
	private static final String INT_CODE= "weatherCode";	
	
	private static final String OBJ_ARR_DES = "weatherDesc";
	private static final String STRING_VALUE = "value";
	
	private static final String OBJ_ARR_WEATHER = "weather";
	
	private static final String STRING_DATE = "date";
	
	private static final String OBJ_ARR_ASTRONOMY = "astronomy";
	private static final String STRING_SUNRISE = "sunrise";
	private static final String STRING_SUNSET = "sunset";
	
	private static final String INT_MAX_TEMP = "maxtempC";
	private static final String INT_MIN_TEMP = "mintempC";
	private static final String INT_AVG_TEMP = "avgtempC";
	
	final String DEGREE = "\u00b0";
	final String CELSIUS = "C";
	
	ArrayList<WeatherData> newForecast;
	JSONObject dataa = null;
	JSONArray current = null;
	JSONArray description = null;
	JSONArray weathr = null;
	
	ImageView icon;
	TextView temp;
	TextView desc;
	ListView lstCustomView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_list);
		
		icon = (ImageView) findViewById(R.id.iv_climate_icon);
		//temp = (TextView) findViewById(R.id.tv_current_temp_date);
		//desc = (TextView) findViewById(R.id.tv_current_temp_date);
		//lstCustomView = (ListView) findViewById(R.id.list_view_date);
		
		Intent intent = getIntent();
		final Bundle val = intent.getExtras();
		String cityName = val.getString("citi");
		
		//ActionBar actionBar = getActionBar();
		//actionBar.setTitle(cityName);
		
		url = "http://api.worldweatheronline.com/premium/v1/weather.ashx?"
				+ "key="+ KEY +"&"
				+ "q="+ cityName +"&"
				+ "num_of_days=7&"
				+ "format=json";
		
		System.out.println(cityName+"ggtydttiiiiiiiiiiiiiiiidddddddddddd");
		
		JSONTask jTask = new JSONTask();
		jTask.execute(url);
		
	}
	
	class JSONTask extends AsyncTask<String,String,String>{
		
		String msg;
		int curTmp;
		int imgcod;
		String val;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url);
			
			try{
				dataa = json.getJSONObject(OBJ_DATA);
				
				JSONArray error = dataa.getJSONArray("error");
				JSONObject o = error.getJSONObject(0);
				 msg = o.getString("msg");
				
				current = dataa.getJSONArray(OBJ_ARR_CURR);
				JSONObject tempry = current.getJSONObject(0);
				curTmp = tempry.getInt(INT_TEMP);
				imgcod = tempry.getInt(INT_CODE);
				
				description = tempry.getJSONArray(OBJ_ARR_DES);
				JSONObject v = description.getJSONObject(0);
				val = v.getString(STRING_VALUE);
				
				weathr = dataa.getJSONArray(OBJ_ARR_WEATHER);
				for (int i = 0; i < weathr.length(); i++) {
					JSONObject w = weathr.getJSONObject(i);
					String dt = w.getString(STRING_DATE);
					int mat = w.getInt(INT_MAX_TEMP);
					int mit = w.getInt(INT_MIN_TEMP);
					int avt = w.getInt(INT_AVG_TEMP);
					
					JSONArray astr = w.getJSONArray(OBJ_ARR_ASTRONOMY);
					JSONObject ast = astr.getJSONObject(0);
					String sr = ast.getString(STRING_SUNRISE);
					String ss = ast.getString(STRING_SUNSET);
					
					WeatherData wthr = new WeatherData();
					wthr.setDate(dt);
					wthr.setMax(mat);
					wthr.setMin(mit);
					wthr.setAvg(avt);
					wthr.setSunrise(sr);
					wthr.setSunset(ss);
					newForecast.add(wthr);
					
				}
								
			}
			catch (JSONException e) {				
				e.printStackTrace();
			}			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
		
				if(msg.equalsIgnoreCase("Unable to find any matching weather location to the query submitted!")){
					Intent intent = new Intent(DateListActivity.this,
							EnterCityActivity.class);
					startActivity(intent);
					Toast.makeText(DateListActivity.this,
							"Please enter a valid city", Toast.LENGTH_LONG).show();
				}
				else{
					temp.setText(curTmp+DEGREE+CELSIUS);
					desc.setText(val);
					if(imgcod == 113)
						icon.setImageResource(R.drawable.clear_day);
					if(imgcod == 116)
						icon.setImageResource(R.drawable.fewclouds);
					else if(imgcod>=119 && imgcod<=143)
						icon.setImageResource(R.drawable.clouds);
					else if(imgcod>=263 && imgcod<=296 && imgcod==176 && imgcod==353)
						icon.setImageResource(R.drawable.drizzle);
					else if(imgcod>=248 && imgcod<=260)
						icon.setImageResource(R.drawable.fog);
					else if(imgcod>=200 && imgcod<=230 && imgcod==386 &&imgcod==392)
						icon.setImageResource(R.drawable.thunder);
					else if(imgcod>=299 && imgcod<=323 && imgcod==356 && imgcod==359 && imgcod==389)
						icon.setImageResource(R.drawable.rain);
					else if(imgcod>=326 && imgcod<=377 && imgcod==179 && imgcod==227  && imgcod==395 )
						icon.setImageResource(R.drawable.snowr);
					else
						icon.setImageResource(R.drawable.odr);
				}
			CustomAdapter customAdapter = new CustomAdapter(DateListActivity.this, newForecast);
			lstCustomView.setAdapter(customAdapter);
			
		}
		
	}

	
}
