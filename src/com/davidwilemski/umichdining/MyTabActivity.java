package com.davidwilemski.umichdining;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

public abstract class MyTabActivity extends TabActivity{
	public static final String PREFS_NAME = "UmichPrefFile";
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.date_picker:
				final Calendar c = Calendar.getInstance();
				DatePickerDialog dp = new DatePickerDialog(this, dateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dp.show();
				break;
			case R.id.refresh:
				DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				new DownloadDataClass(getApplicationContext()).execute(dbMod, settings.getString("DATE", "NONE"));
				break;
			case R.id.prefs:
				Intent settingsActivity = new Intent(getBaseContext(), PrefsView.class);
				startActivity(settingsActivity);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			//Toast.makeText(getApplicationContext(), "You selected: " + year + "/" + monthOfYear + "/" + dayOfMonth + "." , Toast.LENGTH_SHORT);
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			monthOfYear++;
			String month, day;
			if(monthOfYear < 10)
				month = "0" + monthOfYear;
			else
				month = monthOfYear + "";
			if(dayOfMonth < 10)
				day = "0" + dayOfMonth;
			else
				day = dayOfMonth + "";
			editor.putString("DATE", year + "-" + month + "-" + day);
			editor.commit();
			DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
			new DownloadDataClass(getApplicationContext()).execute(dbMod, settings.getString("DATE", "NONE"));
			View w = getCurrentFocus();
			if(w.getContext().toString().contains("com.davidwilemski.umichdining.MealActivity"))
				finish();
		}
	};	
}
