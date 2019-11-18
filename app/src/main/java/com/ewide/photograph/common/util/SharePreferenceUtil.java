package com.ewide.photograph.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SharePreferenceUtil {

	private final static String name = "config";
	private final static int mode = Context.MODE_PRIVATE;

	/**
	 * 
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public final static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public final static void putList(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		Set set = new HashSet();
		set.add(key);
		edit.putStringSet(key, set);
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public final static void putString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * 
	 */
	public final static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * 
	 */
	public final static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getString(key, defValue);
	}

}
