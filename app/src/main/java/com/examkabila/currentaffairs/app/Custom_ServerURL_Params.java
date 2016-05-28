package com.examkabila.currentaffairs.app;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

public class Custom_ServerURL_Params {





	public final static String getCurrent_GK_Read_link() { // int pageNo, String
															// langCode, int
															// AppId, String
															// date, String
															// month, String
															// year,Context con
															// return
															// "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/test.php?pageno="+
															// pageNo+
															// "&langCode="+
															// langCode+
															// "&AppId="+AppId+
															// "&date="+ date+
															// "&month="+ month+
															// "&year=" + year+
															// "&version_code="+Globals.getversion_code(con);
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/test.php";
	}

	public final static String getCurrent_GK_Test_link() {
		// int qNo, String langCode, String date, String month, String
		// year,Context con
		// return
		// "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getGKQuestions.php?QuestionNo="+qNo+"&langCode="+langCode+"&date="+date+"&month="+month+"&year="+year
		// +"&version_code="+Globals.getversion_code(con);
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getQuizQuestion.php";
	}
	public final static String getCurrentNews() {
		// int qNo, String langCode, String date, String month, String
		// year,Context con
		// return
		// "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getGKQuestions.php?QuestionNo="+qNo+"&langCode="+langCode+"&date="+date+"&month="+month+"&year="+year
		// +"&version_code="+Globals.getversion_code(con);
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/get_gk_slide.php";
	}

	public static String getVideoList() {
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getvideo.php";
	}
	public static String getVideoLisRe() {
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getrelated_video.php";
	}
	public static String getMoreApps() {
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/get_more_apps.php";
	}
	public static String getURL_GcmRegister() {

		return "http://xercesblue.in/onlinexamserver/liquid_data/PushNotificationDatabase/gcm_register_gk.php";

	}
	public static String getURL_youtube() {

		return "https://www.youtube.com/channel/UCOYKJU2TiGhZh-cyhBXor3Q?sub_confirmation=1";

	}
	public static String getURL_Download() {

		return "http://www.examkabila.com/Bankpo/bankinggkpdf";

	}

	public static String getAdvertisement_link() {
		return "http://xercesblue.in/onlinexamserver/liquid_data/AdvtControlCenter/getCurrentAffairsAdds_Config.php";
	}

	public static HashMap<String, String> getParams_GK_Test(
			String langCode, String date,Context con) {

		HashMap<String, String> map = new HashMap<String, String>();
		// "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getGKQuestions.php?"
		// +
		// "QuestionNo="+qNo+"&langCode="+langCode+"&date="+date+"&month="+month+"&year="+year
		// +"&version_code="+Globals.getversion_code(con);

		map.put("langCode", langCode);
		if(!date.equals(""))
		map.put("date", date);
		map.put("version_code",1+"");
		Log.i("SUSHIL", "getParams_ --->" + map);
		return map;
	}

	public static HashMap<String, String> getParams_newsSlidet(
			String langCode, String date,Context con) {

		HashMap<String, String> map = new HashMap<String, String>();
		// "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getGKQuestions.php?"
		// +
		// "QuestionNo="+qNo+"&langCode="+langCode+"&date="+date+"&month="+month+"&year="+year
		// +"&version_code="+Globals.getversion_code(con);

		map.put("langCode", langCode);
		if(!date.equals(""))
			map.put("date", date);
		map.put("AppId",1+"");
		Log.i("SUSHIL", "getParams_ --->" + map);
		return map;
	}

	public static HashMap<String, String> getParams_GCMRegister(Context con,String imei) {

		HashMap<String, String> map = new HashMap<String, String>();
		Object_AppConfig config = new Object_AppConfig(con);

		map.put("Imei", imei);
		map.put("regId", config.getRegistrationId(con));

		Log.i("SUSHIL", "getParams_ --->" + map);
		return map;
	}

	public static HashMap<String, String> getParams_GK_Read(int pageNo,
			String langCode, int AppId, String date, String month, String year,
			Context con) {

		HashMap<String, String> map = new HashMap<String, String>();
		// return
		// "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/test.php?"
		// +
		// "pageno="+ pageNo+ "&langCode="+ langCode+ "&AppId="+AppId+ "&date="+
		// date+ "&month="+ month+ "&year=" + year+
		// "&version_code="+Globals.getversion_code(con);
		map.put("pageno", pageNo + "");
		map.put("langCode", langCode);
		map.put("AppId", AppId + "");
		map.put("date", date);
		map.put("month", month);
		map.put("year", year);
		map.put("version_code", 10+"");
		Log.i("SUSHIL", "getParams_ --->" + map);

		return map;
	}

}
