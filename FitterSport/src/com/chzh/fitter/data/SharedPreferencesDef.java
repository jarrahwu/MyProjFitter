package com.chzh.fitter.data;

/**
 * 声明一些sharedPreferences 调用get的时候获取失败的时候的一些默认值
 */
public interface SharedPreferencesDef {

	/**
	 * 数字类的默认值 float int long
	 */
	public static final int DEFALUT_VALUE = -0xab;

	/**
	 * 字符串的默认值
	 */
	public static final String DEFALUT_STRING = "N/A";

	/**
	 * bool的默认值
	 */
	public static final boolean DEFALUT_BOOL = false;
}
