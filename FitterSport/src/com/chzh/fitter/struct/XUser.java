package com.chzh.fitter.struct;

import com.chzh.fitter.data.AbstractPreferencesData;
import com.chzh.fitter.data.JSharedPreference;
import com.jarrah.json.JProperty;

/**
 * JSON 用户信息  支持写入preferences
 */
public class XUser extends AbstractPreferencesData{

	@JSharedPreference
	@JProperty(key="nickname")
	String nickname;

	@JSharedPreference
	@JProperty(key="pic")
	String portrait;

	@JSharedPreference
	@JProperty(key="mobile")
	String mobile;

	@JSharedPreference
	@JProperty(key="sex")
	int gender;

	@JSharedPreference
	@JProperty(key="height")
	float height;

	@JSharedPreference
	@JProperty(key="weight")
	float weight;

	@JSharedPreference
	@JProperty(key="age")
	int age;

	@JSharedPreference
	@JProperty(key="bmi")
	float bmi;

	@JSharedPreference
	@JProperty(key="accesstoken")
	String token;
	
	@JProperty(key="stitle")
	@JSharedPreference(key="active_status")
	String activeStatus;
	
	@JProperty(key="fritotal")
	@JSharedPreference(key="friend_count")
	int friendCount;
	
	

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getMoblie() {
		return mobile;
	}

	public void setMoblie(String mobile) {
		this.mobile = mobile;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getBmi() {
		return bmi;
	}

	public void setBmi(float bmi) {
		this.bmi = bmi;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNickName() {
		return nickname;
	}

	public void setNickName(String nickName) {
		this.nickname = nickName;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public void printValues() {
		for (int i = 0; i < getDefineFields().length; i++) {
			try {
				Object value = getDefineFields()[i].get(this);
				System.out.println(getDefineFields()[i].getName() + " : " +value.toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}


}
