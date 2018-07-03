package payal.cluebix.www.ecommerce.Handlers;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import payal.cluebix.www.ecommerce.CenterActivity;
import payal.cluebix.www.ecommerce.Login;

public class SessionManager {

	SharedPreferences pref;
	Editor editor;// Editor for Shared preferences
	Context _context;
	int PRIVATE_MODE = 0;	// Shared pref mode
	/*Base_url.id, Base_url.name,Base_url.email,Base_url.mobile,Base_url.created_date,updated*/
	private static final String PREF_NAME = "MyPreference";// Sharedpref file name
	private static final String IS_LOGIN = "IsLoggedIn";	// All Shared Preferences Keys

	public static final String KEY_NAME = "name";// User name
	public static final String KEY_ID = "id";//
	public static final String KEY_email = "mail";// Email address
	public static final String KEY_mobile = "mobile";// Email address
	public static final String KEY_createDate = "created date";// Email address
	public static final String KEY_LastModified = "modified date";// Email address
	public static final String KEY_UserName = "uname";//


	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */

	public void createLoginSession(String id, String name, String uname, String email, String mobile, String created, String modified){

		Log.d("sessionscreen",id+name+email+mobile+created+modified);
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_ID, id);
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_UserName, uname);
		editor.putString(KEY_email, email);
		editor.putString(KEY_mobile, mobile);
		editor.putString(KEY_createDate, created);
		editor.putString(KEY_LastModified, modified);


		editor.commit();
	}	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(this.isLoggedIn()){
			Log.d("islogged",this.isLoggedIn()+"");
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, CenterActivity.class);
			i.putExtra("cartTransition","dash");
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Staring Login Activity
			_context.startActivity(i);
		}
		else{
			Intent i = new Intent(_context, Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}

	}

	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_ID, pref.getString(KEY_ID, null));
		user.put(KEY_UserName,pref.getString(KEY_UserName,null));
		user.put(KEY_email, pref.getString(KEY_email, null));
		user.put(KEY_mobile, pref.getString(KEY_mobile, null));
		user.put(KEY_createDate, pref.getString(KEY_createDate, null));
		user.put(KEY_LastModified, pref.getString(KEY_LastModified, null));

		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, Login.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
