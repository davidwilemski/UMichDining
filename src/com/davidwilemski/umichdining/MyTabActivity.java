package com.davidwilemski.umichdining;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
		//System.out.println(item.getItemId());
		switch (item.getItemId()) {
			case R.id.date_picker:
				//Toast.makeText(getApplicationContext(), "You found the date picker! :)", Toast.LENGTH_SHORT).show();
				final Calendar c = Calendar.getInstance();
				DatePickerDialog dp = new DatePickerDialog(this, dateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dp.show();
				break;
			case R.id.refresh:
				DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
				//Toast.makeText(getApplicationContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
				//dbMod.fetchData();
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				new DownloadDataClass(getApplicationContext()).execute(dbMod, settings.getString("DATE", "NONE"));
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
			editor.putString("DATE", year + "-" + monthOfYear + "-" + dayOfMonth);
			editor.commit();
		}
	};	
}
