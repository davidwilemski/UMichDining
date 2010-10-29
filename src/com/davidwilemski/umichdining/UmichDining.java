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
        intent = new Intent().setClass(this, CentralCampusTab.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("central").setIndicator("Central Campus",
                          res.getDrawable(android.R.drawable.ic_dialog_alert))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        //Central Campus Tab
        intent = new Intent().setClass(this, HillTab.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("hill").setIndicator("Hill Area",
                          res.getDrawable(android.R.drawable.ic_dialog_alert))
                      .setContent(intent);
        tabHost.addTab(spec);
        
      //Central Campus Tab
        intent = new Intent().setClass(this, NorthCampusTab.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("north").setIndicator("North Campus",
                          res.getDrawable(android.R.drawable.gallery_thumb))
                      .setContent(intent);
        tabHost.addTab(spec);
    }
}