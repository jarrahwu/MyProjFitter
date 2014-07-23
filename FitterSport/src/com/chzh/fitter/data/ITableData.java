package com.chzh.fitter.data;

import android.content.ContentValues;

/**
 * 表数据
 */
public interface ITableData {

	/**
	 * 获取表名
	 * @return
	 */
	String getTableName();

	/**
	 * 获取键值数据,可以参照这样的写法,主要方便数据的增删改查<br/>
	 *
	 * 	ContentValues values = new ContentValues();<br/>
	 *	values.put("name", getName()); <br/>
	 *	values.put("color", getColor()); <br/>
	 *	return values; <br/>
	 *
	 * @return
	 */
	ContentValues getValues();
}
