package com.jarrah.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 *  JSON解析器
 *	JSON 支持的数据类型: <br/>
 *	int, double, boolean, String,long
 *  还有json object json array
 */
public class XSON {

	private static final String SETTER_DEFAULT_PRIFIX = "set";

	//需要转型的tag
	public static final int NO_TYPE_CAST = 0xff;
	public static final int DOUBLE_TO_FLOAT = NO_TYPE_CAST + 1;
	public static final int INTEGER_TO_INT = DOUBLE_TO_FLOAT + 1;

	/**
	 * invoke the method in your java bean class
	 * @param methodOwner
	 * @param methodName
	 * @param value
	 */
	public <T extends Object> void invokeBeanMethod(Object methodOwner, String methodName, T value) {
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

	/**
	 * get the fields declared in the bean class
	 * @param bean
	 * @return the field array
	 */
	public <T extends Object> Field[] getBeanFields(T bean) {
		return bean.getClass().getDeclaredFields();
	}

	public String getDefaultBeanSetter(Field field) {
		return SETTER_DEFAULT_PRIFIX + getCapitalFieldName(field.getName());
	}

	@SuppressLint("DefaultLocale")
	private String getCapitalFieldName(String fieldName) {
		String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return name;
	}


	/**
	 * get the filter fields by {@link JProperty}
	 * @param owner the fields' owner
	 * @return the fields
	 */
	public Field[] getJPropertyFields(Object owner) {
		Field[] fields = owner.getClass().getDeclaredFields();
		java.util.List<Field> flist = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if(field.isAnnotationPresent(JProperty.class)){
				flist.add(field);
			}
		}
		Field[] a = new Field[flist.size()];
		return flist.toArray(a );
	}

	/**
	 * convert json to javabean
	 * @param owner JAVA bean class
	 * @param obj json data
	 * @return
	 * @throws IllegalAccessException
	 */
	public <T extends Object> T fromJSON(T owner, JSONObject obj) {
		Field[] fields = getJPropertyFields(owner);
		for (int i = 0; i < fields.length; i++) {
			String key = getJSONKey(fields[i]);

			Object value = null;
			try {
				value = getJSONValueWith(fields[i],obj);
			} catch (JSONException e1) {
				e1.printStackTrace();
				Log.e("XSON", "json error at : " + key);
			}finally {
				setField(owner, fields[i], value);
			}
		}
		return owner;
	}

	/**
	 * 设置变量值 , 如果value null 不赋值
	 * @param owner
	 * @param field
	 * @param value
	 */
	public void setField(Object owner, Field field, Object value) {

		if(value == null) return;

		field.setAccessible(true);

		switch (getCastType(field, value)) {
		case NO_TYPE_CAST:
			setFieldForExcept(field, owner, value);
			break;

		case DOUBLE_TO_FLOAT:
			float floatValue = Float.valueOf(value.toString());
			setFieldForExcept(field, owner, floatValue);
			break;

		case INTEGER_TO_INT:
			setFieldForExcept(field, owner, value);
			break;

		default:
			break;
		}

	}

	private void setFieldForExcept(Field field, Object owner, Object value) {
		try {
			field.set(owner, value);
		} catch (IllegalArgumentException e) { //一般都是转型错误
			e.printStackTrace();

			try { //出现转型异常,设置value 为null
				Log.e("XSON", "except : set value null");
				field.set(owner, null);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private Object getJSONValueWith(Field field, JSONObject obj)
			throws JSONException {
		JProperty property = field.getAnnotation(JProperty.class);
		Object value = null;
		// 说明不是嵌套数据
		if (property.subObjectName().length() == 0) {
			value = obj.get(property.key());
		}
		// 嵌套数据
		if (property.subObjectName().length() > 0) {
			String[] keys = getSubJsonKeyBy(property.subObjectName());
			JSONObject subObj = obj;

			// 获取嵌套的子jsonObject
			for (int i = 0; i < keys.length; i++) {
				subObj = subObj.getJSONObject(keys[i]);
			}

			// 获取对应的value
			value = subObj.get(property.key());
		}

		return value;
	}

	/**
	 * 类型是否匹配
	 * @param field
	 * @param value
	 * @return
	 */
	private int getCastType(Field field, Object value) {
		if (field.getType() == value.getClass()) {
			return NO_TYPE_CAST;
		}
		if (field.getType() == int.class && value.getClass() == Integer.class) {
			return NO_TYPE_CAST;
		}

		if (field.getType() == double.class && value.getClass() == Double.class) {
			return NO_TYPE_CAST;
		}

		if (field.getType() == float.class && value.getClass() == Double.class) {
			return DOUBLE_TO_FLOAT;
		}

		if (field.getType() == long.class && value.getClass() == Long.class) {
			return NO_TYPE_CAST;
		}

		if (field.getType() == boolean.class && value.getClass() == Boolean.class) {
			return NO_TYPE_CAST;
		}
		return NO_TYPE_CAST;
	}

	/**
	 * 获取有JProperty标志的field对应的JSON key
	 * @param field
	 * @return
	 */
	public String getJSONKey(Field field) {
		if (field.isAnnotationPresent(JProperty.class)) {
			return field.getAnnotation(JProperty.class).key();
		}
		return null;
	}

	/**
	 * 根据key包含的 "." 的个数,判断这个json有多少层数据
	 * @param parentKey
	 * @see JProperty
	 * @return 如果嵌套数据关键字的数组 数据的嵌套层数 = 返回的数据的长度 + 1;
	 */
	public String[] getSubJsonKeyBy(String parentKey) {
		return parentKey.split("\\.");
	}
 }
