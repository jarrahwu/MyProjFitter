package com.chzh.fitter.struct;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.util.JSONUtil;


public class UserInfo extends AbstractUserPreference<UserInfo> implements GlobalConstant{

	public UserInfo() {
	}

	public UserInfo(JSONObject obj) {
		parseUserJson(obj);
	}


	/**
	 * 解释JSON
	 * @param jsonObject
	 */
	public void parseUserJson(JSONObject jsonObject) {
		String avatarUrl = JSONUtil.getString(jsonObject, "portrait");
		String nick = JSONUtil.getString(jsonObject, "nick");
		String account = JSONUtil.getString(jsonObject, "mobile");
		int gender = JSONUtil.getInt(jsonObject, "gender");
		float height = (float) JSONUtil.getDouble(jsonObject, "height");
		float weight = (float) JSONUtil.getDouble(jsonObject, "weight");
		String birthday = JSONUtil.getString(jsonObject, "birth_day");
		float bmi = (float) JSONUtil.getDouble(jsonObject, "bmi");
		String token = JSONUtil.getString(jsonObject, "token");
		int age = JSONUtil.getInt(jsonObject, "age");

		// setinfo
		avatarUrl(avatarUrl).nick(nick).account(account).gender(gender)
				.height(height).weight(weight).birthday(birthday).bmi(bmi)
				.token(token).age(age);
	}

	@Override
	protected Field[] getDefineFields() {
		return getClass().getSuperclass().getDeclaredFields();
	}

	@SuppressLint("SimpleDateFormat")
	public int getAgeByBirthday(String birthday) {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			String currentTime = formatDate.format(calendar.getTime());
			Date today = formatDate.parse(currentTime);
			Date brithDay = formatDate.parse(birthday);

			return today.getYear() - brithDay.getYear();
		} catch (Exception e) {
			return 0;
		}
	}
}
