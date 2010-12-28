package com.davidwilemski.umichdining;

import junit.framework.Assert;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TwitterAuthActivity extends Activity {
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
	
	SharedPreferences settings = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settings = this.getSharedPreferences(PREFS_NAME, 0);
		
		provider.setOAuth10a(true);	
		
		String authUrl = null;
		if(this.getIntent().getData() == null) { // Means we're not coming back from the web page...
			try {
				Toast.makeText(getBaseContext(), "Please authorize UMichDining to access your twitter account!", Toast.LENGTH_SHORT).show();
				authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
				saveRequestInfo(settings, consumer.getToken(), consumer.getTokenSecret());
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			} catch (OAuthMessageSignerException e) {
				toastIt(getBaseContext(), e.getMessage().toString());
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				toastIt(getBaseContext(), e.getMessage().toString());
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				toastIt(getBaseContext(), e.getMessage().toString());
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				toastIt(getBaseContext(), e.getMessage().toString());
				e.printStackTrace();
			}
		}
	}
	
	protected void onResume() {
		super.onResume();
		Uri uri = this.getIntent().getData();  
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {  
			String token = settings.getString("REQUEST_TOKEN", null);
			String secret = settings.getString("REQUEST_SECRET", null);
		    // this will populate token and token_secret in consumer  
		    try {
		    	if(!(token == null || secret == null)) 
		    		consumer.setTokenWithSecret(token, secret);
		    	
		    	String otoken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
			    String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			    
			    // This should never fail
			    Assert.assertEquals(otoken, consumer.getToken());
			    
				provider.retrieveAccessToken(consumer, verifier);
				
				token = consumer.getToken();
				secret = consumer.getTokenSecret();
				
				saveAuthenticationInfo(settings, token, secret);
				saveRequestInfo(settings, null, null);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("TWITTER", true);
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
			} finally {
				startActivity(new Intent(this, PrefsView.class));
				finish(); // need this.
			}
		}
	}
	
	/*
	 * Custom helper functions.
	 */
	public static void saveRequestInfo(SharedPreferences thisSettings, String token, String secret) {
		SharedPreferences.Editor editor = thisSettings.edit();
		if(token == null) {
			// Clear it out
			editor.remove("REQUEST_TOKEN");
			Log.d("OAUTH", "Cleared request token.");
		} else {
			editor.putString("REQUEST_TOKEN", token);
			Log.d("OAUTH", "Saved request token: " + token);
		}
		if(secret == null) {
			// Clear it out
			editor.remove("REQUEST_SECRET");
			Log.d("OAUTH", "Cleared request secret.");
		} else {
			editor.putString("REQUEST_SECRET", secret);
			Log.d("OAUTH", "Saved request secret: " + secret);
		}
		editor.commit();
	}
	
	public static void saveAuthenticationInfo(SharedPreferences thisSettings, String token, String secret) {
		SharedPreferences.Editor editor = thisSettings.edit();
		if(token == null) {
			// Clear it out
			editor.remove("USER_TOKEN");
			Log.d("OAUTH", "Cleared OAuth token.");
		} else {
			editor.putString("USER_TOKEN", token);
			Log.d("OAUTH", "Saved OAuth token: " + token);
		}
		if(secret == null) {
			// Clear it out
			editor.remove("USER_SECRET");
			Log.d("OAUTH", "Cleared OAuth secret.");
		} else {
			editor.putString("USER_SECRET", secret);
			Log.d("OAUTH", "Saved OAuth secret: " + secret);
		}
		editor.commit();
	}
	
	public static void toastIt(Context ctx, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}
}
