package com.davidwilemski.umichdining;

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
        		menu + " TEXT)");
        
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertRecord(String newlocation, String newdate, String newmenu) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Check to make sure that the information isn't in the db already
		// If it is, we update.
		Cursor c = this.getLocationMeal(newlocation, newdate);
		if(!c.moveToFirst()) {
			ContentValues cv = new ContentValues();
			cv.put(location, newlocation);
			cv.put(date, newdate);
			cv.put(menu, newmenu);
			db.insert(tbName, null, cv);
			db.close();
			return;
		} else {
			ContentValues cv = new ContentValues();
			cv.put(menu, newmenu);
			db.update(tbName, cv, location + "=? AND " + date + "=?" , new String[]{newlocation, newdate});
		}
	}
	
	public Cursor getLocationMeal(String currLocation, String currDate) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.query(tbName, new String[]{menu, id}, location + "=? AND " + date + "=?", new String[]{currLocation, currDate}, null, null, null);
		return cur;
	}
}