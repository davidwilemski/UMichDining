package com.davidwilemski.umichdining;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class NorthCampusTab extends ListActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CentralCampus));
		 
		
		 ListView lv = getListView();
		 lv.setTextFilterEnabled(true);
		 
		 lv.setOnItemClickListener(new OnItemClickListener(){
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			 {
				 
			
				 /* Will be filled and displayed later. */
				 	//String[] entrees = new String[30];
					try {
						
						String urlLocation = "http://roadrunner.davidwilemski.com/UMichDining/sample.json";
						String response = "";
						String line = "";
						URL url = new URL(urlLocation);
						BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
						
						while((line = input.readLine()) != null) {
							response += line;
						}
						try{
							JSONArray jArray = new JSONArray(response);
							System.out.println(jArray.get(0).toString());
							JSONArray place = new JSONArray(jArray.get(0).toString());
							Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							System.out.println(e.getMessage());
						}
						
						
					}
					catch (Exception e) {
						System.out.println("XML Pasing Excpetion = " + e);
					}

					
					
					
					

				 
				//startActivity(intent);				 
			 }
		 });

		
	}
	static final String[] CentralCampus = new String[] {
		   "Betsy Barbour", "East Quad", "South Quad", "West Quad", "North Quad"
		  };
}
