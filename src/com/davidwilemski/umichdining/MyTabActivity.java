package com.davidwilemski.umichdining;

import java.util.Calendar;

import com.davidwilemski.umichdining.DownloadDataClass;

import android.app.DatePickerDialog;
import android.app.TabActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

public abstract class MyTabActivity extends TabActivity{
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
				new DownloadDataClass(getApplicationContext()).execute(dbMod);
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
			Toast.makeText(getApplicationContext(), "You selected: " + year + "/" + monthOfYear + "/" + dayOfMonth + "." , Toast.LENGTH_SHORT);
		}
	};
	
	protected static String date_string = "";
	
}
