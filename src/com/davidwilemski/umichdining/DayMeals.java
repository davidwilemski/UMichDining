package com.davidwilemski.umichdining;


import java.util.Calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class DayMeals extends MyTabActivity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String b = getIntent().getExtras().getString("b");
        String l = getIntent().getExtras().getString("l");
        String d = getIntent().getExtras().getString("d");
        
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Breakfast Tab
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MealActivity.class);
        intent.putExtra("data", b);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("breakfast").setIndicator("Breakfast",
                          res.getDrawable(R.drawable.ic_tab_breakfast))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Lunch Tab
        intent = new Intent().setClass(this, MealActivity.class);
        intent.putExtra("data", l);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("lunch").setIndicator("Lunch",
                          res.getDrawable(R.drawable.ic_tab_lunch))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Dinner Tab
        intent = new Intent().setClass(this, MealActivity.class);
        intent.putExtra("data", d);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("dinner").setIndicator("Dinner",
                          res.getDrawable(R.drawable.ic_tab_dinner))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        //Set the current tab
        tabHost.setCurrentTab(getMealTab());
    }
    //Check if there are SharedPreferences for when meals switch
    //otherwise return defaults
    //return the tab number based on what time it is
    int getMealTab(){
    	//check for preferences
    	SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
    
    	Calendar c = Calendar.getInstance();
        if(c.get(Calendar.HOUR_OF_DAY) < sp.getInt("breakfast", 10))
        	return 0;
        else if(c.get(Calendar.HOUR_OF_DAY) < sp.getInt("lunch", 15))
        	return(1);
        else if(c.get(Calendar.HOUR_OF_DAY) >= sp.getInt("dinner", 15))
        	return(2);
        else //Default to breakfast
        	return 0;
    }
}
