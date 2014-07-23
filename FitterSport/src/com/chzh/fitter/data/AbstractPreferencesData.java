package com.chzh.fitter.data;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 继承这个类可以用于SPHelper方便储存为 SharedPreferences数据,<b>只能声明需要储存的变量，其他的不能在这里声明。</b>
 * 支持的数据类型为 <b>int String long float boolean </b> 5种
 */
public abstract class AbstractPreferencesData implements IPreferencesData{

	 /**
	  * 获取变量名
	 * @return
	 */
	public String[] getVarNames() {

//		Field[] fields = getClass().getFields();
		Field[] fields = getDefineFields();

		String[] varNames = new String[fields.length];

		for (int i = 0; i < fields.length; i++) {
			System.out.println("变量名：" + fields[i].getName());
			if (fields[i].getAnnotation(JSharedPreference.class).key().length() == 0) {
				varNames[i] = fields[i].getName();
			} else {
				varNames[i] =fields[i].getAnnotation(JSharedPreference.class).key();
			}
			
		}

		return varNames;
	 }

	/**
	 * 获取变量类型
	 * @return
	 */
	public Class<?>[] getVarTypes() {

//		Field[] fields = getClass().getDeclaredFields();
		Field[] fields = getDefineFields();

		Class<?>[] varTypes = new Class<?>[fields.length];

		for (int i = 0; i < varTypes.length; i++) {
//			System.out.println("变量类型：" + fields[i].getType());
			varTypes[i] = fields[i].getType();
		}

		return varTypes;
	}

	/**
	 * 获取变量值
	 * @return
	 */
	public Object[] getVarValues() {

		//Field[] fields = getClass().getDeclaredFields();
		Field[] fields = getDefineFields();

		Object[] values = new Object[fields.length];

		for (int i = 0; i < values.length; i++) {
			try {
				fields[i].setAccessible(true);
				values[i] = fields[i].get(this);
//				System.out.println("变量值：" +values[i]);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	/**
	 * 获取类重定义的变量数组.特殊情况:如果A class extends {@link AbstractPreferencesData},那么如果B extends A 的话
	 * 会导致这个数组为空.因为这个方法只是获取当前的类定义的变量.
	 * 要想解决,可以在B类中的这个方法里这样重写这个方法:
	 * protected Field[] getDefineFields() {
	 *	return this.getClass().getSuperclass().getDeclaredFields();
	 * }
	 * @return JSharedPreference注解 变量数组.
	 */
	protected Field[] getDefineFields() {

		List<Field> fl = new ArrayList<Field>();
		Field[] fields = getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(JSharedPreference.class)) {
				fl.add(fields[i]);
			}
		}
		return fl.toArray(new Field[fl.size()]);
	}

}
