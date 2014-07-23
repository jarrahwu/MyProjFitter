package com.chzh.fitter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据匹配器
 */
public class DataMatcher {


	public static DataMatcher mInstance;

	public static synchronized DataMatcher getInstance() {
		if (mInstance == null) {
			mInstance = new DataMatcher();
		}
		return mInstance;
	}

	/**
	 * 判断数据长度是否在这个范围
	 * @param data 要验证的数据
	 * @param min 最小
	 * @param max 最大
	 * @return
	 */
	public boolean isDataLengthInRange(String data, int min, int max) {
		if(data == null || data.equals("") || data.length() == 0) {
			return false;
		}

		if (data.length() >= min && data.length() <= max) {
			return true;
		}

		return false;
	}

	/**
	 * 判断是否为电话格式
	 * @param phone
	 * @return
	 */
	public boolean isPhoneNumberFormat(String phone) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(phone);

		System.out.println("is phone : " + m.matches());

		return m.matches();
	}

	/**
	 * 判断是否为电子邮件格式
	 * @param email
	 * @return
	 */
	public boolean isEmailFormat(String email){

        boolean isExist = false;
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if(b) {
            System.out.println("is email!");
            isExist=true;
        } else {
            System.out.println("is not email");
        }
        return isExist;
    }


	/**
	 * 判断字符串是否为empty 如果是 null 或者 长度为 0 就是empty
	 *
	 * @return
	 */
	public boolean isStringEmpty(String str) {
		if (str == null || str.equals("") || str.length() == 0) {
			return true;
		}
		return false;
	}


}
