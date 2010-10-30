package com.davidwilemski.umichdining;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LunchActivity extends ListActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	     try {
			JSONArray data = new JSONArray(getIntent().getExtras().getString("data"));
			String[] NewMenu = new String[data.length()];
			for(int i = 0; i < data.length(); i++) {
				NewMenu[i] = data.getString(i);
			}
		     
			 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NewMenu));
			 
			 ListView lv = getListView();
			 lv.setTextFilterEnabled(true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Something went wrong in LunchActivity.", Toast.LENGTH_SHORT).show();
		}
	}
}