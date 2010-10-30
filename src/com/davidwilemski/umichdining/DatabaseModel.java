package com.davidwilemski.umichdining;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseModel extends SQLiteOpenHelper {
	static final String dbName = "umichdining";
	static final String tbName = "menus";
	static final String id = "id";
	static final String location = "location";
	static final String date = "date";
	static final String meal = "meal";
	static final String menu = "menu";
	
	public DatabaseModel(Context context) {
		super(context, dbName, null, 1); // Version number is the last one
	}
	
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tbName + " (" +
        		id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		location + " TEXT, " +
        		date + " TEXT, " +
        		meal + " TEXT, " +
        		menu + " TEXT)");
        
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertRecord(String newlocation, String newdate, String newmeal, String newmenu) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Check to make sure that the information isn't in the db already
		// If it is, we update.
		Cursor c = this.getLocationMeal(newlocation, newdate, newmeal);
		if(!c.moveToFirst()) {
			ContentValues cv = new ContentValues();
			cv.put(location, newlocation);
			cv.put(date, newdate);
			cv.put(meal, newmeal);
			cv.put(menu, newmenu);
			db.insert(tbName, null, cv);
		} else {
			/*ContentValues cv = new ContentValues();
			cv.put(menu, newmenu);
			int i = db.update(tbName, cv, location + "=? AND " + date + "=?" , new String[]{newlocation, newdate});
			System.out.println("Changed " + i);*/
		}
		c.close();
		db.close();
	}
	
	public Cursor getLocationMeal(String currLocation, String currDate, String currMeal) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.query(tbName, new String[]{menu, id, meal}, location + "=? AND " + date + "=? AND " + meal + "=?", new String[]{currLocation, currDate, currMeal}, null, null, null);
		//db.close();
		return cur;
	}
	
	@SuppressWarnings("unchecked")
	public void fetchData(Context context) {
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
				JSONObject jO = new JSONObject(response);
				//System.out.println(jArray.get(0).toString());
				Iterator it = jO.keys();
				while(it.hasNext()) {
					String item = (String) it.next();
					JSONObject place = jO.optJSONObject(item);
					//System.out.println(item);
					this.insertRecord(item, "2010-11-02", "breakfast", place.getString("breakfast").toString());
					//System.out.println(place.getString("breakfast").toString());
					this.insertRecord(item, "2010-11-02", "lunch", place.getString("lunch").toString());
					this.insertRecord(item, "2010-11-02", "dinner", place.getString("dinner").toString());
				}
				//JSONObject place = new JSONObject(jO.getString("Bursley"));
			} catch (JSONException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			// TODO Error message
			//Toast.makeText(this, "Something went wrong Fetching the Data.", Toast.LENGTH_SHORT).show();
		}
	}
}