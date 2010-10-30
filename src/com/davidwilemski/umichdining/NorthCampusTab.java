package com.davidwilemski.umichdining;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.database.Cursor;
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
				 DatabaseModel dbMod = new DatabaseModel(getApplicationContext());
				 dbMod.insertRecord("Bursley", "10/26/2010", "MENU_DATA");
				 
				 Cursor c = dbMod.getLocationMeal("Bursley", "10/26/2010");
				 c.moveToFirst();
				 Toast.makeText(getApplicationContext(), c.getString(0), Toast.LENGTH_LONG).show();
				 /* Will be filled and displayed later. */
				 	//String[] entrees = new String[30];
					try {
						
						String urlLocation = "http://roadrunner.davidwilemski.com/UMichDining/index.php";
						String response = "";
						String line = "";
						URL url = new URL(urlLocation);
						BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
						
						while((line = input.readLine()) != null) {
							response += line;
						}
						//response += "";
						try{
							JSONObject jO = new JSONObject(response);
							//System.out.println(jArray.get(0).toString());
							JSONObject place = new JSONObject(jO.getString("Bursley"));
							Toast.makeText(getApplicationContext(), place.getString("Breakfast").toString(), Toast.LENGTH_LONG).show();
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
		   "Bursley"
		  };
}
