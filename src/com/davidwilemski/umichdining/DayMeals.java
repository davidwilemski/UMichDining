package com.davidwilemski.umichdining;


import android.content.Intent;
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
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
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
    }
}
