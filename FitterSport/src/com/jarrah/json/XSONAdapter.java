package com.jarrah.json;

import java.lang.reflect.Field;


public class XSONAdapter {

	public XSONAdapter() {
	}

	public  Class<?> getFieldType(Field field) {

		if (field.getType() == Integer.TYPE) {

		}else if (field.getType() == Double.TYPE) {

		}else if (field.getType() == Float.TYPE) {
			return Double.TYPE;
		}else if (field.getType() == Boolean.TYPE) {

		}
		return Integer.class;
	}

	public Object castType(Class<?> type, Object value) {

		if(value.getClass() == String.class)
		{

		}

		if (type == Integer.TYPE) {
			int ret = (Integer) value;
			return ret;
		}

//		if (type ) {
//
//		}

//		if (type == ) {
//
//		}

		return null;
	}

//	public void test(JSONObject jsonObject) {
//		jsonObject.getInt(name)
//		jsonObject.getBoolean(name);
//		jsonObject.getDouble(name);
//		jsonObject.getJSONArray(name);
//		jsonObject.getLong(name)
//		jsonObject.getString(name)
//		jsonObject.get(name);
//		jsonObject.get
//	}

}
