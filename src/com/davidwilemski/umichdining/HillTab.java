package com.davidwilemski.umichdining;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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


public class HillTab extends ListActivity{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		 setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Hill));
		 
		
		 ListView lv = getListView();
		 lv.setTextFilterEnabled(true);
		 
		lv.setOnItemClickListener(new OnItemClickListener(){
			 public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			 {
			
				 /* Will be filled and displayed later. */
				 	String[] entrees = new String[30];
					try {

						URL url = new URL(
								"http://housing.umich.edu/files/helper_files/js/menu2xml.php?location=BURSLEY%20DINING%20HALL&date=tomorrow");
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						Document doc = db.parse(new InputSource(url.openStream()));
						doc.getDocumentElement().normalize();
						
						//narrow document down to only meal tags
						NodeList nodeList = doc.getElementsByTagName("meal");
						
						for(int j = 0; j<nodeList.getLength(); j++){
							Node node = nodeList.item(j);
							Element tst = (Element) node;
							NodeList menuitems = tst.getElementsByTagName("menuitem");
							
							
							for (int i = 0; i < menuitems.getLength(); i++) {
	
								Element fstElmnt = (Element) node;
								NodeList nameList = fstElmnt.getElementsByTagName("menuitem");
								Element nameElement = (Element) nameList.item(i);
								nameList = nameElement.getChildNodes();
								entrees[i] = ((Node) nameList.item(0)).getNodeValue();
						//		entrees[i] =  menuitems.item(i).getNodeValue();
	
								Toast.makeText(getApplicationContext(), entrees[i], Toast.LENGTH_SHORT).show();
							}
						}
					}
					catch (Exception e) {
						System.out.println("XML Pasing Excpetion = " + e);
					}

					
					
					
					

				 
				//startActivity(intent);				 
			 }
		 });

		
	}
	static final String[] Hill = new String[] {
		   "Mary Markley", "Marketplace at HDC", "Oxford"
		  };
}
