package com.davidwilemski.umichdining;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownloadDataClass extends AsyncTask<DatabaseModel, Void, Integer> {	
	protected Context ctx;
	protected int dd;
	protected DownloadDataClass(Context c){
		ctx = c;
	}
	protected void onPreExecute() {
		
	}
	
	protected Integer doInBackground(DatabaseModel... params) {
		DatabaseModel db = params[0];
		Integer r = db.fetchData();
		return r;
	}
	
	protected void onPostExecute(Integer response) {
		Toast.makeText(ctx, "Information Loaded!", Toast.LENGTH_SHORT).show();
		if(response != 0) {
			Toast.makeText(ctx, "Something went wrong getting data.", Toast.LENGTH_SHORT).show();
		}
	}
}