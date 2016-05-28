package com.examkabila.currentaffairs.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Object_AppConfig {

	
	private Context context;
	
	private final String KEY_APP_CONFIG = "appConfig";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor = null;
	
	public Object_AppConfig(Context context){
		
		this.context = context;
		prefs = this.context.getSharedPreferences(KEY_APP_CONFIG, 0);
		editor = prefs.edit();
	}

	public String getLangCode() {
		String LangCode = "" ;

		if(prefs != null)
			LangCode = prefs.getString("appConfig_LangCode","");

		return LangCode;
	}
	public void setLangCode(String LangCode) {

		if (editor != null) {
			editor.putString("appConfig_LangCode", LangCode);
			editor.commit();
		}

	}

	public String getDateTime() {
		String DateTime = "" ;

		if(prefs != null)
			DateTime = prefs.getString("appConfig_DateTime","");

		return DateTime;
	}
	public void setDateTime(String DateTime) {

		if (editor != null) {
			editor.putString("appConfig_DateTime", DateTime);
			editor.commit();
		}

	}

	public String getYouTubeKey() {
		String YouTubeKey = "" ;

		if(prefs != null)
			YouTubeKey = prefs.getString("appConfig_YouTubeKey","");

		return YouTubeKey;
	}
	public void setYouTubeKey(String YouTubeKey) {

		if (editor != null) {
			editor.putString("appConfig_YouTubeKey", YouTubeKey);
			editor.commit();
		}

	}
	public int isLanguageSelect() {
		int lanid = 0;
		
		if(prefs != null)
			lanid = prefs.getInt("appConfig_lanid", 0);
		
		return lanid;
	}

	public void SetisLanguageSelect(int lanid) {

		if (editor != null) {
			editor.putInt("appConfig_lanid", lanid);
			editor.commit();
		}

	}

	public void SetshowAdds(int showAdds) {

		if (editor != null) {
			editor.putInt("appConfig_showAdds", showAdds);
			editor.commit();
		}

	}

	public int getshowAdds() {
		int showAdds = 0;

		if(prefs != null)
			showAdds = prefs.getInt("appConfig_showAdds", 0);

		return showAdds;
	}

	public void SetadTypeInterId(int adTypeInterId) {

		if (editor != null) {
			editor.putInt("appConfig_adTypeInterId", adTypeInterId);
			editor.commit();
		}

	}

	public int getadTypeInterId() {
		int adTypeInterId = 0;

		if(prefs != null)
			adTypeInterId = prefs.getInt("appConfig_adTypeInterId", 0);

		return adTypeInterId;
	}


	public void SetadTypeId(int adTypeId) {

		if (editor != null) {
			editor.putInt("appConfig_adTypeId", adTypeId);
			editor.commit();
		}

	}

	public int getadTypeId() {
		int adTypeId = 0;

		if(prefs != null)
            adTypeId = prefs.getInt("appConfig_adTypeId", 0);

		return adTypeId;
	}


	public boolean isNotificationEnabled() {
		boolean notificationEnabled = true;
		if(prefs != null)
			notificationEnabled = prefs.getBoolean("appConfig_NotificationEnabled", true);

		return notificationEnabled;
	}

	public void setNotificationEnabled(boolean notificationEnabled) {

		if (editor != null) {
			editor.putBoolean("appConfig_NotificationEnabled", notificationEnabled);
			editor.commit();
		}
	}

	public boolean isTopBar() {
		boolean Enabled = true;
		if(prefs != null)
			Enabled = prefs.getBoolean("appConfig_Enabled", true);

		return Enabled;
	}

	public void setToBar(boolean Enabled) {

		if (editor != null) {
			editor.putBoolean("appConfig_Enabled", Enabled);
			editor.commit();
		}
	}


	public boolean getboolnavHeader() {
		//false_mean = goes from cate list;
		//true_mean = goes from doctor list ;
		boolean value = false;
		
		if(prefs != null)
			value = prefs.getBoolean("appConfig_value", false);
		
		return value;
	}
	public void setboolnavHeader(boolean value) {
		
		if (editor != null) {
			editor.putBoolean("appConfig_value", value);
			editor.commit();
		}

	}


	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the registration ID in your app is up to you.
		return context.getSharedPreferences(Activity_Splash.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Gets the current registration token for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration token, or empty string if there is no existing
	 *         registration token.
	 */
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString("PROPERTY_REG_ID", "");
		if (registrationId.isEmpty()) {
			Log.i("SUSHIL", "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing registration ID is not guaranteed to work with
		// the new app version.
		int registeredVersion = prefs.getInt("PROPERTY_APP_VERSION", Integer.MIN_VALUE);
		int currentVersion = Globals.getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("SUSHIL", "App version changed.");
			return "";
		}
		return registrationId;
	}

	public void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = Globals.getAppVersion(context);
		Log.i("SUSHIL", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("PROPERTY_REG_ID", regId);
		editor.putInt("PROPERTY_APP_VERSION", appVersion);
		editor.commit();
	}

	
}
