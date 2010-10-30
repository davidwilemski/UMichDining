package com.davidwilemski.umichdining;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class DayMeals extends TabActivity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String b = getIntent().getExtras().getString("b");
        String l = getIntent().getExtras().getString("l");
        String d = getIntent().getExtras().getString("d");
        /*String b = savedInstanceState.getString("b");
        String l = savedInstanceState.getString("l");
        String d = savedInstanceState.getString("d");*/
        
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Breakfast Tab
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, BreakfastActivity.class);
        intent.putExtra("data", b);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("breakfast").setIndicator("Breakfast",
                          res.getDrawable(android.R.drawable.ic_dialog_alert))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Lunch Tab
        intent = new Intent().setClass(this, LunchActivity.class);
        intent.putExtra("data", l);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("lunch").setIndicator("Lunch",
                          res.getDrawable(android.R.drawable.ic_dialog_alert))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Dinner Tab
        intent = new Intent().setClass(this, DinnerActivity.class);
        intent.putExtra("data", d);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("dinner").setIndicator("Dinner",
                          res.getDrawable(android.R.drawable.gallery_thumb))
                      .setContent(intent);
        tabHost.addTab(spec);
    }
}
