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
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MealActivity extends ListActivity{
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

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		settings = getSharedPreferences(PREFS_NAME, 0);

		try {
			JSONArray data = new JSONArray(getIntent().getExtras().getString("data"));
			final String[] NewMenu = new String[data.length()];
			for(int i = 0; i < data.length(); i++) {
				NewMenu[i] = data.getString(i);
			}
		     
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NewMenu));
			
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
					final String message = "Excited for " + NewMenu[arg2] + " at " + getIntent().getExtras().getString("location") + "! #umichdining";
					AlertDialog.Builder builder = new AlertDialog.Builder(arg1.getContext());
					builder.setMessage("Can we post this to twitter?\n\n\"" + message + "\"")
					       .setCancelable(false)
					       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                // Do thing
									//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
									
									String token = settings.getString("USER_TOKEN", null);
									String secret = settings.getString("USER_SECRET", null);
							    	if(!(token == null || secret == null)) 
							    		consumer.setTokenWithSecret(token, secret);
							    	else {
							    		Toast.makeText(getApplicationContext(), "Error tweeting!", Toast.LENGTH_LONG).show();
							    		return;
							    	}
							    	
									HttpPost post = new HttpPost("http://twitter.com/statuses/update.xml");  
									final List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>(); 
									nvps.add(new BasicNameValuePair("status", message));  
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
					       })
					       .setNegativeButton("No", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();
					           }
					       });
					AlertDialog alert = builder.create();
					alert.setTitle("Post to Twitter");
					alert.show();
					return true;
				}
			});
			
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Something went wrong in MealActivity.", Toast.LENGTH_SHORT).show();
		}
	}
}
