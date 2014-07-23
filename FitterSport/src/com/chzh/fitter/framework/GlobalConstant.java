package com.chzh.fitter.framework;

/**
 * 全局变量
 */
public interface GlobalConstant {
//
	//URL constants
	public static final String HOST = "http://admin.togoalad.com/";
	
	public static final String HOST_IP = "http://admin.togoalad.com";
	
//	public static final String HOST = "http://192.168.3.144/";
//	
//	public static final String HOST_IP = "http://192.168.3.144";
	
	public static final String USER_HOST = HOST + "user/";

	public static final String USER_MODIFY = USER_HOST + "upduser";

	public static final String VCODE_URL = USER_HOST + "vcode";

	 /**
	 * 登陆的URL
	 */
	public static final String LOGIN_URL = USER_HOST + "login";

	public static final String REG_URL = USER_HOST + "reg";

	//sharedPreference Constants

	public static final String USER_PREFERENCE = "user_pref";

	public static final String VISITOR_LOGIN_URL = USER_HOST + "vlogin";

	public static final String USER_COURSE_LOG = USER_HOST + "exelog";
	
	public static final String CURRENT_TODO = USER_HOST + "todayp";
	
	public static final String INVITE_PHONE_CONTACTS = USER_HOST + "addfa";
	
	public static final String INVITE_FITTER = USER_HOST + "addf";
	
	public static final String GET_FRIENDS = USER_HOST + "friendl";
	
	public static final String GET_FITTER_FRIENDS = USER_HOST + "invitefl";
	
	public static final String ADD_PLANT = USER_HOST + "addplan";
	
	public static final String USER_PLAN_LOG = USER_HOST + "lookplog";
	
	public static final String USER_SIGN = USER_HOST + "checkin";
	
	public static final String LOGOUT_URL = USER_HOST + "logout";
	
	public static final String GYM_PLAN = USER_HOST + "fal";
	
	public static final String HOME = USER_HOST + "broadcast";
	
	public static final String SPORT_LOG = USER_HOST + "planl";

	public static final String USER_SIGN_ADD = USER_HOST + "addcheckin";

	public static final String APP_ID = "wxc7155981f040612c";
	
	public static final String BAIDU_KEY = "251097dfa1";
	
	public static final String SPORT_LOG_DETAIL = USER_HOST + "exeplan";
}
