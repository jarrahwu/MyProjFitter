package com.chzh.fitter.struct;

import com.jarrah.json.JProperty;

public class AddFitterFriendData {
	
	@JProperty(key="uid")
	String uid;
	
	@JProperty(key="nickname")
	String nickname;
	
	@JProperty(key="mobile")
	String mobile;
	
	@JProperty(key="pic")
	String portrait;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	
	
}
