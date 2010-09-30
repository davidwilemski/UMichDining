package com.davidwilemski.umichdining;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DinnerActivity extends ListActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DinnerMenu));
		 
		 ListView lv = getListView();
		 lv.setTextFilterEnabled(true);
		 
		 lv.setOnItemClickListener(new OnItemClickListener(){
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			 {
				 Toast.makeText(getApplicationContext(), "Do something to this item...", Toast.LENGTH_SHORT).show();
			 }
		 });

		
	}
	
	  static final String[] DinnerMenu = new String[] {
		   "Milk", "Turkey", "Mashed Potatoes", "Gravy", "Stuffing", "Corn Bread"
		  };
	}


