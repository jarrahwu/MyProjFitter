package com.chzh.fitter.data;

public interface IPreferencesData {
//	/**
//	 * 获取变量数，这里返回的数值 一定要与声明的变量的个数一样多
//	 */
//	public abstract int getVarCount();

	 /**
	  * 获取变量名
	 * @return
	 */
	public String[] getVarNames() ;

	/**
	 * 获取变量类型
	 * @return
	 */
	public Class<?>[] getVarTypes();

	/**
	 * 获取变量值
	 * @return
	 */
	public Object[] getVarValues();

}
