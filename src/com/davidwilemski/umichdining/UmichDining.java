package com.davidwilemski.umichdining;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

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
        dbMod.fetchData(getApplicationContext());
		/*dbMod.insertRecord("Bursley", "10/26/2010", "MENU_DATA");
		 
		Cursor c = dbMod.getLocationMeal("Bursley", "10/26/2010");
		c.moveToFirst();
		Toast.makeText(getApplicationContext(), c.getString(0), Toast.LENGTH_LONG).show();*/
    }
    
    static final String[] NorthCampus = new String[] {
		"Bursley"
	};
    
	static final String[] CentralCampus = new String[] {
		   "Betsy Barbour", "East Quad", "South Quad", "West Quad", "North Quad"
	};
	
	static final String[] Hill = new String[] {
		   "Mary Markley", "Marketplace at HDC", "Oxford"
	};
}