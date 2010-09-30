package com.davidwilemski.umichdining;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BreakfastActivity extends ListActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, BreakfastMenu));
		 
		 ListView lv = getListView();
		 lv.setTextFilterEnabled(true);
		 
		 lv.setOnItemClickListener(new OnItemClickListener(){
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			 {
				 //Toast.makeText(getApplicationContext(), "Do something to this item...", Toast.LENGTH_SHORT).show();
				///////// 
				 String html = "Broken";
				 try {  
		                URL updateURL = new URL("http://iconic.4feets.com/update");  
		                URLConnection conn = updateURL.openConnection();  
		                InputStream is = conn.getInputStream();  
		                BufferedInputStream bis = new BufferedInputStream(is);  
		                ByteArrayBuffer baf = new ByteArrayBuffer(50);  
		  
		                int current = 0;  
		                while((current = bis.read()) != -1){  
		                    baf.append((byte)current);  
		                }  
		  
		                /* Convert the Bytes read to a String. */  
		               html = new String(baf.toByteArray());  
		            } catch (Exception e) {  
		            }  
				    /////////
				    
				    Toast.makeText(getApplicationContext(), html, Toast.LENGTH_SHORT).show();
			 }
		 });

		
	}
	
	  static final String[] BreakfastMenu = new String[] {
		   "Cranberry Juice", "Eggs", "Bacon"
		  };
	}


