package com.moaPlatform.moa.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * PreferenceUtils
 */
public class PreferenceUtils {

	public static SharedPreferences getPreference(Context context, String preferenceName) {
		if(context != null) {
			return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
		}
		return null;
	}

	public static SharedPreferences.Editor getPreferenceEditor(Context context, String preferenceName) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		if (sharedPreferences != null) {
			return sharedPreferences.edit();
		}
		return null;
	}

	/**
	 * set string preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @param value
	 */
	public static void setPreference(Context context, String preferenceName, String key, Object value) {
		SharedPreferences.Editor edit = getPreferenceEditor(context, preferenceName);
		if(edit != null) {
			if (value instanceof String) {
				edit.putString(key, (String) value);
			} else if (value instanceof Boolean) {
				edit.putBoolean(key, (Boolean) value);
			} else if (value instanceof Integer) {
				edit.putInt(key, (Integer) value);
			} else if (value instanceof Float) {
				edit.putFloat(key, (Float) value);
			} else if (value instanceof Long) {
				edit.putLong(key, (Long) value);
			}
			edit.commit();
		}
	}

	public static Map<String, ?> getAll(Context context, String preferenceName) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences.getAll();
	}

	public static void remove(Context context, String preferenceName, String value) {
		if(value != null){
			SharedPreferences sharedPreferences = getPreference(context, preferenceName);
			sharedPreferences.edit().remove(value);
			sharedPreferences.edit().commit();
		}
	}

	/**
	 * get boolean preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @return
	 */
	public static boolean getBooleanPreference(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences != null ? sharedPreferences.getBoolean(key, false) : false;
	}

	/**
	 * get string preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @return
	 */
	public static String getStringPreference(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences != null ? sharedPreferences.getString(key, "") : "";
	}

	/**
	 * get boolean preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @return
	 */
	public static boolean getBooleanPreferenceDefaultTrue(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences != null ? sharedPreferences.getBoolean(key, true) : true;
	}

	/**
	 * get integer preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @return
	 */
	public static int getIntPreference(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences != null ? sharedPreferences.getInt(key, 0) : 0;
	}

	/**
	 * get float preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @return
	 */
	public static float getFloatPreference(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences != null ? sharedPreferences.getFloat(key, 0.0f) : 0.0f;
	}

	/**
	 * get long preference
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 * @return
	 */
	public static long getLongPreference(Context context, String preferenceName, String key) {
		SharedPreferences sharedPreferences = getPreference(context, preferenceName);
		return sharedPreferences != null ? sharedPreferences.getLong(key, 0) : 0;
	}

}
