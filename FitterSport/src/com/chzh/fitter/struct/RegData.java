package com.chzh.fitter.struct;

import java.io.Serializable;

public class RegData implements Serializable{

	private static final long serialVersionUID = 2956285726295457727L;

	private String phoneNum; // 电话号码

	private String password; //密码

	private String msgAuthNum;//短信验证code


	public RegData() {}

	public RegData(String phoneNum, String password, String msgAuthNum) {
		this.phoneNum = phoneNum;
		this.password = password;
		this.msgAuthNum = msgAuthNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsgAuthNum() {
		return msgAuthNum;
	}

	public void setMsgAuthNum(String msgAuthNum) {
		this.msgAuthNum = msgAuthNum;
	}

}
