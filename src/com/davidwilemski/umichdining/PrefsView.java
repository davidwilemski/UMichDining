package com.davidwilemski.umichdining;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
	        CONSUMER_KEY, CONSUMER_SECRET);  
	  
	OAuthProvider provider = new CommonsHttpOAuthProvider(
			"http://twitter.com/oauth/request_token", 
			"http://twitter.com/oauth/access_token",  
	        "http://twitter.com/oauth/authorize");  
	
	HttpClient client = new DefaultHttpClient(); 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference twitter_pref = findPreference("TWITTER");
		twitter_pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				boolean new_setting = !settings.getBoolean("TWITTER", false);
				if(new_setting) {
					// We are turning twitter on...
					Toast.makeText(getApplicationContext(), "Turning on Twitter", Toast.LENGTH_SHORT).show();
					String authUrl = null;
					provider.setOAuth10a(true);	
					try {
						authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
						if(consumer.getToken() != null)
							editor.putString("REQUEST_TOKEN", consumer.getToken());
						if(consumer.getTokenSecret() != null)
							editor.putString("REQUEST_SECRET", consumer.getTokenSecret());
						editor.commit();
					} catch (OAuthMessageSignerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthNotAuthorizedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))); 
				} else {
					// We are turning twitter off...
					Toast.makeText(getApplicationContext(), "Turning off Twitter", Toast.LENGTH_SHORT).show();
					editor.putBoolean("TWITTER", false);
				}
				return true;
			}
		});
	}
	
	protected void onResume() {
		super.onResume();
		Uri uri = this.getIntent().getData();  
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {  
		    String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
		    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    // this will populate token and token_secret in consumer  
		    try {
		    	if(!(settings.getString("REQUEST_TOKEN", null) == null || settings.getString("REQUEST_SECRET", null) == null)) {
					consumer.setTokenWithSecret(settings.getString("REQUEST_TOKEN", null), settings.getString("REQUEST_SECRET", null));
				}
				provider.retrieveAccessToken(consumer, verifier);
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("TOKEN: " + consumer.getToken());
		} 
	}
}
