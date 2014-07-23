package com.chzh.fitter.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.widget.SlidingDrawer;

import com.chzh.fitter.R;
import com.chzh.fitter.data.AbstractPreferencesData;
import com.chzh.fitter.data.SharedPreferencesHelper;
import com.chzh.fitter.data.SingleAction;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.JSONUtil;
import com.jarrah.json.XSON;

public class UICore implements GlobalConstant{

	private Context mContext;

	private SharedPreferencesHelper mSharedPreferencesHelper;

	public UICore(Context context) {
		mContext = context;
		mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
	}

	public void saveAsPreferenceData(AbstractPreferencesData data, String targetName) {
		mSharedPreferencesHelper.openOrCreateSharedPreferences(targetName);
		mSharedPreferencesHelper.save(data);
	}

	/**
	 * 从Preference获取用户信息
	 * @return
	 */
	public XUser getUserFromPreference() {
		XUser user = new XUser();
		mSharedPreferencesHelper.openOrCreateSharedPreferences(USER_PREFERENCE);
		Bundle userBundle = mSharedPreferencesHelper.get(user);

		String nickName = userBundle.getString("nickname");
		String portrait = userBundle.getString("portrait");
		String mobile = userBundle.getString("mobile");
		int gender = userBundle.getInt("gender");
		float height = userBundle.getFloat("height");
		float weight = userBundle.getFloat("weight");
		int age = userBundle.getInt("age");
		float bmi = userBundle.getFloat("bmi");
		String wid = userBundle.getString("wid");
		String token = userBundle.getString("token");
		String activeStatus = userBundle.getString("active_status");
		int friendCount = userBundle.getInt("friend_count");

		user.setNickName(nickName);
		user.setPortrait(portrait);
		user.setMoblie(mobile);
		user.setGender(gender);
		user.setHeight(height);
		user.setWeight(weight);
		user.setAge(age);
		user.setBmi(bmi);
		user.setToken(token);
		user.setActiveStatus(activeStatus);
		user.setFriendCount(friendCount);
		
		return user;
	}
	
	public void clearUserInfo() {
		mSharedPreferencesHelper.openOrCreateSharedPreferences(USER_PREFERENCE);
		mSharedPreferencesHelper.clear();
	}

	public String getToken() {
		return getUserFromPreference().getToken();
	}

	/**
	 * 将对象初始化,并且用XSON读取json对象里面的数据,填充到owner
	 * @param owner
	 * @param jsonData
	 */
	public void initDataWithJSON(AbstractPreferencesData owner, JSONObject jsonData) {
		new XSON().fromJSON(owner, jsonData);
	}

	/**
	 * 映射调用方法
	 * @param methodOwner
	 * @param methodName
	 * @param value
	 */
	public <T extends Object> void involeMethod(Object methodOwner, String methodName, T value) {
		try {
			Method method = methodOwner.getClass().getMethod(
					methodName, new java.lang.Class[] {value.getClass()});
			method.invoke(methodOwner, value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


	/** 获取库Phone表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER};
	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 得到手机通讯录联系人信息 **/
	public ArrayList<ContactInfo> getPhoneContacts(Context context) {
		ArrayList<ContactInfo> list = new ArrayList<UICore.ContactInfo>();

		ContentResolver resolver = context.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				ContactInfo info = new ContactInfo();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				info.setPhone(phoneNumber);
				info.setName(contactName);
				list.add(info);
			}
			phoneCursor.close();
		}
		return list;
	}

	/**
	 * @author jarrahwu 联系人信息
	 */
	public class ContactInfo {
		String name;
		String phone;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
	}

	public String getMacAddress() {
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
		    macAddress = info.getMacAddress();
		}
		return macAddress;
	}

	
//	public ArrayList<SingleAction> getCourseInfo() {
//		ArrayList<SingleAction> course = new ArrayList<SingleAction>();
//		JSONArray array = getBaseCourseData();
//		XSON xson = new XSON();
//		for (int i = 0; i < array.length(); i++) {
//			JSONObject obj = JSONUtil.getJsonObjByIndex(array, i);
//			SingleAction action = new SingleAction();
//			action = xson.fromJSON(action, obj);
//			course.add(action);
//		}
//		return course;
//	}

	//获取基础课程的数据
//	public JSONArray getBaseCourseData() {
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < 20; i++) {
//			array.put(genObj(1, "空气跳绳", "30s", R.drawable.ic_air_jump_rope, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.AirJumpRope)));
//		}
//		array.put(genObj(1, "空气跳绳", "30s", R.drawable.ic_air_jump_rope, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.AirJumpRope)));
//		array.put(genObj(2, "跳跃侧拍手", "30s", R.drawable.ic_jumping_jacks, R.raw.b2_a, R.raw.b2_b, R.raw.b2_c, getText(R.string.JumpingJacks)));
//		array.put(genObj(3, "原地后踢腿跑", "30s", R.drawable.ic_butt_kicks, R.raw.b3_a, R.raw.b3_b, R.raw.b3_c, getText(R.string.ButtKicks)));
//		array.put(genObj(4, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(5, "原地深蹲", "20resp", R.drawable.ic_body_weight_squats, R.raw.b5_a, R.raw.b5_b, R.raw.b5_c, getText(R.string.BodyweightSquats)));
//		array.put(genObj(6, "肘部平板支撑", "45s", R.drawable.ic_elbow_plank, R.raw.b6_a, R.raw.b6_b, R.raw.b6_c, getText(R.string.ElbowPlank)));
//		array.put(genObj(7, "单手支撑", "10resp", R.drawable.ic_plank_twists, R.raw.b7_a, R.raw.b7_b, R.raw.b7_c, getText(R.string.PlankTwists)));
//		array.put(genObj(8, "俯卧撑", "10resp", R.drawable.ic_half_pushups, R.raw.b8_a, R.raw.b8_b, R.raw.b8_c, getText(R.string.HalfPushups)));
//		array.put(genObj(9, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(10, "俯地登山步", "45s", R.drawable.ic_mountain_climbers, R.raw.b10_a, R.raw.b10_b, R.raw.b10_c, getText(R.string.MountainClimbers)));
//		array.put(genObj(11, "侧弓步压腿", "10resp", R.drawable.ic_side_lunges, R.raw.b11_a, R.raw.b11_b, R.raw.b11_c, getText(R.string.SideLunges)));
//		array.put(genObj(12, "原地深蹲", "30s", R.drawable.ic_slalom_jumps, R.raw.b12_a, R.raw.b12_b, R.raw.b12_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(13, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(14, "高抬腿", "30s", R.drawable.ic_high_knees, R.raw.b16_a, R.raw.b16_b, R.raw.b16_c, getText(R.string.HighKnees)));
//		array.put(genObj(15, "空气跳绳", "30s", R.drawable.ic_air_jump_rope, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.AirJumpRope)));
//		array.put(genObj(16, "跳跃侧拍手", "30s", R.drawable.ic_jumping_jacks, R.raw.b2_a, R.raw.b2_b, R.raw.b2_c, getText(R.string.JumpingJacks)));
//		array.put(genObj(17, "原地后踢腿跑", "30s", R.drawable.ic_butt_kicks, R.raw.b17_a, R.raw.b17_b, R.raw.b17_c, getText(R.string.ButtKicks)));
//		return array;
//	}

//	private String getText(int resId) {
//		return mContext.getText(resId).toString();
//	}
//
//	private JSONObject genObj(int actionId, String actionName, String actionCount,
//			int actionImage, int actionPreview, int actionIntroduce, int actionMain, String actionDescription) {
//		JSONObject obj0 = new JSONObject();
//		try {
//			obj0.put("action_id", actionId);
//			obj0.put("action_title", actionName);
//			obj0.put("action_count", actionCount);
//			obj0.put("action_image", actionImage);
//			obj0.put("action_preview", actionPreview);
//			obj0.put("action_introduce", actionIntroduce);
//			obj0.put("action_main", actionMain);
//			obj0.put("action_description", actionDescription);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return obj0;
//	}

	public JSONArray getEmptyJsonArray(int i) {
		JSONArray array = new JSONArray();
		for (int j = 0; j < i; j++) {
			array.put(new JSONObject());
		}
		return array;
	}
	
	//
	private static final int BODY_COURSE = 1;
	private static final int MOTION_COMPRESSION = 2;
	private static final int DYNAMIC_FITNESS = 3;
	private static final int HIIT = 4;
	private static final int STRENGTH_EXCERSICE = 5;
	
//	private JSONArray getCourseDataByIndex(int index) {
//		if (index == 0) {
//			return getBaseCourseData();
//		}
//		JSONArray array = null;
//		switch (index) {
//		case BODY_COURSE:
//			array = getBodyCourseData();
//			break;
//			
//		case MOTION_COMPRESSION:
//			array = getMotionCompressionData();
//			break;
//			
//		case DYNAMIC_FITNESS:
//			array = getDynamicFitnessData();
//			break;
//			
//		case HIIT:
//			array = getHIITData();
//			break;
//			
//		case STRENGTH_EXCERSICE:
//			array = getStrengthExerciseData();
//			break;
//
//		default:
//			break;
//		}
//		return array;
//	}

//	private JSONArray getHIITData() {
//		JSONArray array = new JSONArray();
//		array.put(genObj(1, "俯地起立跳跃", "30s", R.drawable.ic_burpees, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Burpees)));
//		array.put(genObj(2, "原地深蹲", "30s", R.drawable.ic_slalom_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(3, "肘部平板支撑", "45s", R.drawable.ic_elbow_plank, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.ElbowPlank)));
//		array.put(genObj(4, "跳跃侧拍手", "30s", R.drawable.ic_jumping_jacks, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.JumpingJacks)));
//		array.put(genObj(5, "仰卧起坐", "45s", R.drawable.ic_crunches, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Crunches)));
//		array.put(genObj(6, "靠墙原地深蹲", "30s", R.drawable.ic_wall_squat_hold, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.WallSquatHold)));
//		array.put(genObj(7, "膝盖支撑俯卧撑", "20resp", R.drawable.ic_kneeling_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.KneelingPushups)));
//		array.put(genObj(8, "高抬腿", "30s", R.drawable.ic_high_knees, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HighKnees)));
//		return array;
//	}
//
//	private JSONArray getStrengthExerciseData() {
//		JSONArray array = new JSONArray();
//		array.put(genObj(1, "踢拉伸展", "30s", R.drawable.ic_warrior_kickout, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.WarriorKickout)));
//		array.put(genObj(2, "俯卧撑", "10resp", R.drawable.ic_half_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HalfPushups)));
//		array.put(genObj(3, "原地跳跃", "30s", R.drawable.ic_slalom_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(4, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(5, "仰卧起坐", "45s", R.drawable.ic_crunches, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Crunches)));
//		array.put(genObj(6, "靠墙原地深蹲", "30s", R.drawable.ic_wall_squat_hold, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.WallSquatHold)));
//		array.put(genObj(7, "膝盖支撑俯卧撑", "20resp", R.drawable.ic_kneeling_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.KneelingPushups)));
//		array.put(genObj(8, "高抬腿", "30s", R.drawable.ic_high_knees, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HighKnees)));
//		array.put(genObj(9, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(10, "抱膝跳", "30s", R.drawable.ic_tuck_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.TuckJump)));
//		array.put(genObj(11, "俯地登山步", "45s", R.drawable.ic_mountain_climbers, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MountainClimbers)));
//		array.put(genObj(12, "左弓步压腿", "45s", R.drawable.ic_heisman_lunges_eft, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HeismanLungesLeft)));
//		array.put(genObj(13, "右弓步压腿", "45s", R.drawable.ic_heisman_lunges_right, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HeismanLungesLeft)));
//		
//		return array;
//	}
//
//	private JSONArray getDynamicFitnessData() {
//		JSONArray array = new JSONArray();
//		array.put(genObj(1, "高抬腿", "30s", R.drawable.ic_high_knees, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HighKnees)));
//		array.put(genObj(2, "弓步压腿", "30s", R.drawable.ic_lunges, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Lunges)));
//		array.put(genObj(3, "靠墙原地深蹲", "30s", R.drawable.ic_wall_squat_hold, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.WallSquatHold)));
//		array.put(genObj(4, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(5, "原地半蹲跑", "45s", R.drawable.ic_fast_feet_in_squat, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.FastFeetInSquat)));
//		array.put(genObj(6, "左侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_left, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksLeft)));
//		array.put(genObj(7, "右侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_right, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksRight)));
//		array.put(genObj(8, "肘部平板支撑", "45s", R.drawable.ic_elbow_plank, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.ElbowPlank)));
//		array.put(genObj(9, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		
//		array.put(genObj(10, "抱膝跳", "30s", R.drawable.ic_tuck_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.TuckJump)));
//		array.put(genObj(11, "俯地登山步", "45s", R.drawable.ic_mountain_climbers, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MountainClimbers)));
//		array.put(genObj(12, "左弓步压腿", "45s", R.drawable.ic_heisman_lunges_eft, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HeismanLungesLeft)));
//		array.put(genObj(13, "右弓步压腿", "45s", R.drawable.ic_heisman_lunges_right, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HeismanLungesLeft)));
//		array.put(genObj(14, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//			
//		array.put(genObj(15, "原地深蹲", "30s", R.drawable.ic_slalom_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(16, "肘部平板支撑", "45s", R.drawable.ic_elbow_plank, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.ElbowPlank)));
//		array.put(genObj(17, "跳跃侧拍手", "30s", R.drawable.ic_jumping_jacks, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.JumpingJacks)));
//		array.put(genObj(18, "仰卧起坐", "45s", R.drawable.ic_crunches, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Crunches)));
//		array.put(genObj(19, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		
//		array.put(genObj(20, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(21, "泰拳练习", "30s", R.drawable.ic_muay_thai_kicks, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MuayThaiKicks)));
//		array.put(genObj(22, "原地半蹲跑", "45s", R.drawable.ic_fast_feet_in_squat, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.FastFeetInSquat)));
//		array.put(genObj(23, "左侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_left, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksLeft)));
//		array.put(genObj(24, "右侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_right, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksRight)));
//		
//		return array;
//	}
//
//	private JSONArray getMotionCompressionData() {
//		JSONArray array = new JSONArray();
//		array.put(genObj(1, "踢拉伸展", "30s", R.drawable.ic_warrior_kickout, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.WarriorKickout)));
//		array.put(genObj(2, "俯卧撑", "10resp", R.drawable.ic_half_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HalfPushups)));
//		array.put(genObj(3, "原地跳跃", "30s", R.drawable.ic_slalom_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(4, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(5, "蜘蛛侠俯卧撑", "30s", R.drawable.ic_spiderman_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SpidermanPushups)));
//		array.put(genObj(6, "三头肌撑体", "30s", R.drawable.ic_bench_dips, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(7, "俯卧撑", "10resp", R.drawable.ic_half_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HalfPushups)));
//		array.put(genObj(8, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(9, "原地深蹲", "30s", R.drawable.ic_slalom_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(10, "高抬腿", "30s", R.drawable.ic_high_knees, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HighKnees)));
//		array.put(genObj(11, "弓步压腿", "30s", R.drawable.ic_lunges, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Lunges)));
//		array.put(genObj(12, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(13, "原地半蹲跑", "45s", R.drawable.ic_fast_feet_in_squat, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.FastFeetInSquat)));
//		array.put(genObj(14, "俯地登山步", "45s", R.drawable.ic_mountain_climbers, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MountainClimbers)));
//		array.put(genObj(15, "翘臀俯卧撑", "10resp", R.drawable.ic_pike_press, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.PikePress)));
//		array.put(genObj(16, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(17, "蜘蛛侠俯卧撑", "30s", R.drawable.ic_spiderman_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SpidermanPushups)));
//		array.put(genObj(18, "俯地登山步", "45s", R.drawable.ic_mountain_climbers, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MountainClimbers)));
//		array.put(genObj(19, "仰卧起坐", "45s", R.drawable.ic_crunches, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Crunches)));
//		array.put(genObj(20, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(21, "左侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_left, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksLeft)));
//		array.put(genObj(22, "右侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_right, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksRight)));
//		return array;
//	}
//
//	private JSONArray getBodyCourseData() {
//		JSONArray array = new JSONArray();
//		array.put(genObj(1, "空气跳绳", "30s", R.drawable.ic_air_jump_rope, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.AirJumpRope)));
//		array.put(genObj(2, "跳跃侧拍手", "30s", R.drawable.ic_jumping_jacks, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.JumpingJacks)));
//		array.put(genObj(3, "靠墙原地深蹲", "30s", R.drawable.ic_wall_squat_hold, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.WallSquatHold)));
//		array.put(genObj(4, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(5, "三头肌撑体", "30s", R.drawable.ic_bench_dips, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(6, "高抬腿", "30s", R.drawable.ic_high_knees, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.HighKnees)));
//		array.put(genObj(7, "弓步压腿", "30s", R.drawable.ic_lunges, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.Lunges)));
//		array.put(genObj(8, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(9, "俯地登山步", "45s", R.drawable.ic_mountain_climbers, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MountainClimbers)));
//		array.put(genObj(10, "膝盖支撑俯卧撑", "20resp", R.drawable.ic_kneeling_pushups, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.KneelingPushups)));
//		array.put(genObj(11, "原地深蹲", "30s", R.drawable.ic_slalom_jumps, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SlalomJumps)));
//		array.put(genObj(12, "休息", "30s", R.drawable.ic_rest, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, ""));
//		array.put(genObj(13, "泰拳练习", "30s", R.drawable.ic_muay_thai_kicks, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.MuayThaiKicks)));
//		array.put(genObj(14, "原地半蹲跑", "45s", R.drawable.ic_fast_feet_in_squat, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.FastFeetInSquat)));
//		array.put(genObj(15, "左侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_left, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksLeft)));
//		array.put(genObj(16, "右侧肘部平板支撑", "10resp", R.drawable.ic_side_planks_right, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.SidePlanksRight)));
//		array.put(genObj(17, "肘部平板支撑", "45s", R.drawable.ic_elbow_plank, R.raw.b1_a, R.raw.b1_b, R.raw.b1_c, getText(R.string.ElbowPlank)));
//		return array;
//	}
}
