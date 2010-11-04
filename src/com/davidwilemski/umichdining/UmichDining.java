package com.davidwilemski.umichdining;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.Toast;

public class UmichDining extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        //North Campus Tab
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, CampusTab.class);
        intent.putExtra("array", CentralCampus);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("central").setIndicator("Central Campus",
                          res.getDrawable(R.drawable.ic_tab_central))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        //Central Campus Tab
        intent = new Intent().setClass(this, CampusTab.class);
        intent.putExtra("array", Hill);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("hill").setIndicator("Hill Area",
                          res.getDrawable(R.drawable.ic_tab_hill))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        //Central Campus Tab
        intent = new Intent().setClass(this, CampusTab.class);
        intent.putExtra("array", NorthCampus);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("north").setIndicator("North Campus",
                          res.getDrawable(R.drawable.ic_tab_north))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Create the database and get data!
        DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
        //dbMod.fetchData();
        new DownloadDataClass().execute(dbMod);
        
        /*dbMod.insertRecord("Bursley", "10/26/2010", "MENU_DATA");
		 
		Cursor c = dbMod.getLocationMeal("Bursley", "10/26/2010");
		c.moveToFirst();
		Toast.makeText(getApplicationContext(), c.getString(0), Toast.LENGTH_LONG).show();*/
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, 0, 0, "Choose Date");
        menu.add(0, 1, 1, "Refresh (Today)");
        menu.add(0, 2, 2, "About");
        
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	//System.out.println(item.getItemId());
	    switch (item.getItemId()) {
	       case 0:
	    	   //Toast.makeText(getApplicationContext(), "You found the date picker! :)", Toast.LENGTH_SHORT).show();
	    	   final Calendar c = Calendar.getInstance();
	    	   DatePickerDialog dp = new DatePickerDialog(this, dateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	    	   dp.show();
	    	   break;
	       case 1:
	           DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
	           //Toast.makeText(getApplicationContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
	           //dbMod.fetchData();
	           new DownloadDataClass().execute(dbMod);
	           break;
	       case 2: 
	    	   Toast.makeText(getApplicationContext(), "We're glad you found this button fun to click.", Toast.LENGTH_SHORT).show();
	    	   break;
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
    
    static final String[] NorthCampus = new String[] {
		"Bursley"
	};
    
	static final String[] CentralCampus = new String[] {
		   "Betsy Barbour", "East Quad", "South Quad", "West Quad", "North Quad"
	};
	
	static final String[] Hill = new String[] {
		   "Mary Markley", "Marketplace at HDC", "Oxford"
	};
	
	public class DownloadDataClass extends AsyncTask<DatabaseModel, Void, Integer> {
		private final ProgressDialog dialog = new ProgressDialog(UmichDining.this);

		protected void onPreExecute() {
			this.dialog.setMessage("Fetching Data");
			this.dialog.show();
		}
		
		protected Integer doInBackground(DatabaseModel... params) {
			DatabaseModel db = params[0];
			Integer r = db.fetchData();
			return r;
		}
		
		protected void onPostExecute(Integer response) {
			if(this.dialog.isShowing())
				this.dialog.dismiss();

			if(response != 0) {
				Toast.makeText(getApplicationContext(), "Something went wrong getting data.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}