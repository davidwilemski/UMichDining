package com.davidwilemski.umichdining;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.Toast;

public class UmichDining extends TabActivity {
	private boolean dd = false;
	private static boolean initalLoad = false;
	private static String date_string = "";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		    
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab
		
		// Central Campus Tab
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, CampusTab.class);
		intent.putExtra("array", getResources().getStringArray(R.array.CentralCampus));
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("central").setIndicator("Central Campus",
			res.getDrawable(R.drawable.ic_tab_central))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// Hill Campus Tab
		intent = new Intent().setClass(this, CampusTab.class);
		intent.putExtra("array", getResources().getStringArray(R.array.Hill));
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("hill").setIndicator("Hill Area",
			res.getDrawable(R.drawable.ic_tab_hill))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// North Campus Tab
		intent = new Intent().setClass(this, CampusTab.class);
		intent.putExtra("array", getResources().getStringArray(R.array.NorthCampus));
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("north").setIndicator("North Campus",
			res.getDrawable(R.drawable.ic_tab_north))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// Create the database and get data!
		DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
		if(!dd) {
			if(!initalLoad) {
				new DownloadDataClass().execute(dbMod);
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.date_picker:
				final Calendar c = Calendar.getInstance();
				DatePickerDialog dp = new DatePickerDialog(this, dateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dp.show();
				break;
			case R.id.refresh:
				DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
				new DownloadDataClass().execute(dbMod);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			monthOfYear++;
			date_string = year + "-" + monthOfYear + "-" + dayOfMonth;
		}
	};
	
	public class DownloadDataClass extends AsyncTask<DatabaseModel, Void, Integer> {		
		protected void onPreExecute() {
			dd = true;
		}
		
		protected Integer doInBackground(DatabaseModel... params) {
			DatabaseModel db = params[0];
			Integer r = db.fetchData();
			return r;
		}
		
		protected void onPostExecute(Integer response) {
			Toast.makeText(getApplicationContext(), "Information Loaded!", Toast.LENGTH_SHORT).show();
			dd = false;
			initalLoad = true;
			if(response != 0) {
				initalLoad = false;
				Toast.makeText(getApplicationContext(), "Something went wrong getting data.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}