package com.davidwilemski.umichdining;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CentralCampusTab extends ListActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CentralCampus));
		 
		
		 ListView lv = getListView();
		 lv.setTextFilterEnabled(true);
		 final Intent intent = new Intent();
		 intent.setClass(this, DayMeals.class);
		 
		 lv.setOnItemClickListener(new OnItemClickListener(){
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			 {
				 DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
				 
				 try {
					 Cursor c = dbMod.getLocationMeal(CentralCampus[position], "2010-11-02", "breakfast");
					 String b, l, d;
					 if(c.moveToFirst()) {
						 b = c.getString(0);
					 } else {
						 b = "[\"No Menu Avaliable\"]";
					 }
					 
					 c = dbMod.getLocationMeal(CentralCampus[position], "2010-11-02", "lunch");
					 if(c.moveToFirst()) {
						 l = c.getString(0);
					 } else {
						 l = "[\"No Menu Avaliable\"]";
					 }
					 
					 c = dbMod.getLocationMeal(CentralCampus[position], "2010-11-02", "dinner");
					 if(c.moveToFirst()) {
						 d = c.getString(0);
					 } else {
						 d = "[\"No Menu Avaliable\"]";
					 }
					 
					 c.close();
					 intent.putExtra("b", b);
					 intent.putExtra("l", l);
					 intent.putExtra("d", d);
					 startActivity(intent);
					 
					 
				 } catch (Exception e) {
					 // TODO Error
					Toast.makeText(getApplicationContext(), "Something went wrong in NorthCampusTab.", Toast.LENGTH_SHORT).show();
				 }
				 
			 }
		 });
	}
	
	static final String[] CentralCampus = new String[] {
		   "Betsy Barbour", "East Quad", "South Quad", "West Quad", "North Quad"
	};
}
