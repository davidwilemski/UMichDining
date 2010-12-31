package com.davidwilemski.umichdining;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class PrefsView extends PreferenceActivity {
	public static final String PREFS_NAME = "UmichPrefFile";
	public static final String CONSUMER_KEY = "TGy9UGMp5t2WbJQ8ThWYWQ";
	public static final String CONSUMER_SECRET = "omL1ZoIF2ehXv6yXbvwov5DuexG5bxgNbye62BNIB0w";
	public static final String CALLBACK_URL = "umichdining://twitter";
	
	CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(  
	        CONSUMER_KEY, CONSUMER_SECRET);;
	  
	OAuthProvider provider = new CommonsHttpOAuthProvider(
			"http://twitter.com/oauth/request_token", 
			"http://twitter.com/oauth/access_token",  
	        "http://twitter.com/oauth/authorize");;  
	
	HttpClient client = new DefaultHttpClient();
	
	SharedPreferences settings = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		Preference twitter_pref = findPreference("TWITTER");
		twitter_pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences.Editor editor = settings.edit();
				boolean new_setting = !settings.getBoolean("TWITTER", false);
				if(new_setting) {
					// We are turning twitter on...
					editor.putBoolean("TWITTER", false);
					editor.commit();
					Toast.makeText(getBaseContext(), "Turning on Twitter", Toast.LENGTH_SHORT).show();
					String token = settings.getString("USER_TOKEN", null);
					String secret = settings.getString("USER_SECRET", null);
					if(token == null || secret == null) // Then we need to authenticate
						startActivity(new Intent(getApplicationContext(), TwitterAuthActivity.class));
					else { // We have authentication stuff - just  enable it
						editor.putBoolean("TWITTER", true);
						editor.commit();
					}
				} else {
					// We are turning twitter off...
					Toast.makeText(getBaseContext(), "Turning off Twitter", Toast.LENGTH_SHORT).show();
					editor.putBoolean("TWITTER", false);
					editor.commit();
				}
				return true;
			}
		});
	}
	
	protected void onResume() {
		super.onResume();
		
		String token = settings.getString("USER_TOKEN", null);
		String secret = settings.getString("USER_SECRET", null);
    	if(!(token == null || secret == null)) 
    		consumer.setTokenWithSecret(token, secret);
    	
	}
}
