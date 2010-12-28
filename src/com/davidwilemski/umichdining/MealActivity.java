package com.davidwilemski.umichdining;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MealActivity extends ListActivity{
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
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, final View arg1, int arg2, long arg3) {
					AlertDialog.Builder builder = new AlertDialog.Builder(arg1.getContext());
					builder.setMessage("Can we post this to twitter?")
					       .setCancelable(false)
					       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                // Do thing
					        	   Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_LONG).show();
					           }
					       })
					       .setNegativeButton("No", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					           }
					       });
					AlertDialog alert = builder.create();
					alert.setTitle("Post to Twitter");
					alert.show();
					return true;
				}
			});
			
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Something went wrong in MealActivity.", Toast.LENGTH_SHORT).show();
		}
	}
}
