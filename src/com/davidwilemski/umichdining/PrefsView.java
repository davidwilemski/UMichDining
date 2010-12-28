package com.davidwilemski.umichdining;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.widget.Toast;

public class PrefsView extends PreferenceActivity {
	public static final String PREFS_NAME = "UmichPrefFile";
	public static final String CONSUMER_KEY = "TGy9UGMp5t2WbJQ8ThWYWQ";
	public static final String CONSUMER_SECRET = "omL1ZoIF2ehXv6yXbvwov5DuexG5bxgNbye62BNIB0w";
	public static final String CALLBACK_URL = "umichdining://twitter";
	
	CommonsHttpOAuthConsumer consumer = null;
	  
	OAuthProvider provider = null;  
	
	HttpClient client = null; 
	
	SharedPreferences settings = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		consumer = new CommonsHttpOAuthConsumer(  
		        CONSUMER_KEY, CONSUMER_SECRET);
		
		provider = new CommonsHttpOAuthProvider(
				"http://twitter.com/oauth/request_token", 
				"http://twitter.com/oauth/access_token",  
		        "http://twitter.com/oauth/authorize");
		
		client = new DefaultHttpClient();
		
		addPreferencesFromResource(R.xml.preferences);
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		Preference twitter_pref = findPreference("TWITTER");
		twitter_pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences.Editor editor = settings.edit();
				boolean new_setting = !settings.getBoolean("TWITTER", false);
				if(new_setting) {
					// We are turning twitter on...
					Toast.makeText(getBaseContext(), "Turning on Twitter", Toast.LENGTH_SHORT).show();
					String token = settings.getString("USER_TOKEN", null);
					String secret = settings.getString("USER_SECRET", null);
					if(token == null || secret == null) // Then we need to authenticate
						startActivity(new Intent(getApplicationContext(), TwitterAuthActivity.class));
				} else {
					// We are turning twitter off...
					Toast.makeText(getBaseContext(), "Turning off Twitter", Toast.LENGTH_SHORT).show();
					editor.putBoolean("TWITTER", false);
					editor.commit();
				}
				return true;
			}
		});
		
		Preference twitter_test = findPreference("TWITTER_TEST");
		twitter_test.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				String token = settings.getString("USER_TOKEN", null);
				String secret = settings.getString("USER_SECRET", null);
				if(!(token == null || secret == null)) { // We have authentication
					HttpPost post = new HttpPost("http://twitter.com/statuses/update.xml");  
					final List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>(); 
					nvps.add(new BasicNameValuePair("status", "Another Test Message."));  
					try {
						post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e3) {
						e3.printStackTrace();
					}  
					// set this to avoid 417 error (Expectation Failed)  
					post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false); 
					
					// sign the request  
					try {
						Log.d("OAUTH", "Used OAuth token: " + consumer.getToken());
						Log.d("OAUTH", "Used OAuth secret: " + consumer.getTokenSecret());
						consumer.sign(post);
					} catch (OAuthMessageSignerException e2) {
						e2.printStackTrace();
					} catch (OAuthExpectationFailedException e2) {
						e2.printStackTrace();
					} catch (OAuthCommunicationException e2) {
						e2.printStackTrace();
					}  
					// send the request  
					HttpResponse response = null;
					try {
						response = client.execute(post);
					} catch (ClientProtocolException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}  
					// response status should be 200 OK  
					int statusCode = response.getStatusLine().getStatusCode();  
					final String reason = response.getStatusLine().getReasonPhrase();  
					// release connection  
					try {
						response.getEntity().consumeContent();
					} catch (IOException e) {
						e.printStackTrace();
					}  
					if (statusCode != 200) {  
					    Log.d("TwitterConnector", reason);  
					    Toast.makeText(getBaseContext(), reason, Toast.LENGTH_SHORT).show();
					    //throw new OAuthNotAuthorizedException();  
					} else {
					    Toast.makeText(getBaseContext(), "Post successful!", Toast.LENGTH_SHORT).show();
					}
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
