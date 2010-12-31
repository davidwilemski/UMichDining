package com.davidwilemski.umichdining;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class UmichDining extends MyTabActivity {
	private static boolean initalData = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		    
		Resources res = getResources(); // Resource object to get Drawables - no use initializing this if we don't use it everywhere...
		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab
		
		// Central Campus Tab
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, CampusTab.class);
		intent.putExtra("array", res.getStringArray(R.array.CentralCampus));
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("central").setIndicator("Central Campus",
			res.getDrawable(R.drawable.ic_tab_central))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// Hill Campus Tab
		intent = new Intent().setClass(this, CampusTab.class);
		intent.putExtra("array", res.getStringArray(R.array.Hill));
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("hill").setIndicator("Hill Area",
			res.getDrawable(R.drawable.ic_tab_hill))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// North Campus Tab
		intent = new Intent().setClass(this, CampusTab.class);
		intent.putExtra("array", res.getStringArray(R.array.NorthCampus));
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("north").setIndicator("North Campus",
			res.getDrawable(R.drawable.ic_tab_north))
				.setContent(intent);
		tabHost.addTab(spec);
		
		// Initialize settings
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("DATE", dateFormat.format(date));
		editor.commit();
	    
		// Create the database and get data!
		DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
		if(!initalData){
			dbMod.clearDatabase();
			new DownloadDataClass(getApplicationContext()).execute(dbMod, settings.getString("DATE", "NONE"));
			initalData = true;
		}
		
		//String title_date = settings.getString("DATE", "NONE");
		
		//this.setTitle(title_date + " > UMich Dining");
	}
}
