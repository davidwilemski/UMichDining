package com.davidwilemski.umichdining;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownloadDataClass extends AsyncTask<Object, Void, Integer> {	
	protected Context ctx;
	protected int dd;
	protected DownloadDataClass(Context c){
		ctx = c;
	}
	protected void onPreExecute() {
		
	}
	
	protected Integer doInBackground(Object... params) {
		DatabaseModel db = (DatabaseModel) params[0];
		String date = (String) params[1];
		Integer r = db.fetchData(date);
		return r;
	}
	
	protected void onPostExecute(Integer response) {
		Toast.makeText(ctx, "Information Loaded!", Toast.LENGTH_SHORT).show();
		if(response != 0) {
			Toast.makeText(ctx, "Something went wrong getting data.", Toast.LENGTH_SHORT).show();
		}
	}
}