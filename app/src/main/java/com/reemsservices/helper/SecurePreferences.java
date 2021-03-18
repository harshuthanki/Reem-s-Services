package com.reemsservices.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class SecurePreferences {

	public static void savePreferences(Context context, String key, String value)
	{
		SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key,value);
		editor.commit();

	}

	public static void savePreferences(Context context, String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void savePreferences(Context context, String key,
			Boolean value) {
		SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getStringPreference(Context context, String key) {
		String value = "";
		if (context != null) {
			SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
			value = preferences.getString(key,"");
		}
		return value;
	}

	public static int getIntegerPreference(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		int value = preferences.getInt(key, 0);
		return value;
	}

	public static boolean getBooleanPreference(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		boolean value = preferences.getBoolean(key, false);
		return value;
	}

	public static void clearSecurepreference(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		preferences.edit().clear().commit();
	}

	public static boolean isOnline(final Context ctx) {
		if(ctx != null) {
			final ConnectivityManager cm = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				return ni.isConnectedOrConnecting();
			} else {
				return false;
			}
		}
		return false;
	}

	public static void toastMsg(Context context,String msg)
	{
		Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
	}
}