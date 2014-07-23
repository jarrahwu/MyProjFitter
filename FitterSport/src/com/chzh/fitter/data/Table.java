package com.chzh.fitter.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 表，包含增删改查4个功能
 */
public class Table {

	private SQLiteDatabase mSqLiteDatabase;

	private String mTableName;

	public Table(SQLiteDatabase database, String tableName) {
		mSqLiteDatabase = database;
		mTableName = tableName;
	}

	/**
	 * insert into tbName values(?,?,?)
	 * @param args
	 */
	public void insert(Object... args){

		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(mTableName + " ");
		sb.append("VALUES (");

		for (int i = 0; i < args.length; i++) {

			sb.append("?");

			if (i != args.length - 1) {
				sb.append(", ");
			}
		}

		sb.append(")");
		Log.e(this.getClass().getName(), "insert sql = :" + sb.toString());
		mSqLiteDatabase.execSQL(sb.toString(), args);
	};

	/**
	 * 插入表数据
	 * @param values
	 */
	public void insert(ContentValues... values) {

		mSqLiteDatabase.beginTransaction();

		for (int i = 0; i < values.length; i++) {
			mSqLiteDatabase.insert(mTableName, null,
					values[i]);
		}
		mSqLiteDatabase.setTransactionSuccessful(); // 设置事务成功完成
		mSqLiteDatabase.endTransaction();
	};


	/**
	 * 插入表数据
	 * @param tableDatas
	 */
	public void insert(ITableData... tableDatas) {
		for (int i = 0; i < tableDatas.length; i++) {
			insert(tableDatas[i].getValues());
		}
	}

	/**
	 * 更新数据
	 *  update(data , "name = ?, address = ?", "jack", "bj");
	 * @param whereClause
	 * @param whereArgs
	 * @param tableData
	 */
	public void update(ITableData tableData, String whereClause, String... whereArgs) {
		mSqLiteDatabase.beginTransaction();

		mSqLiteDatabase.update(mTableName, tableData.getValues(), whereClause, whereArgs);

		mSqLiteDatabase.setTransactionSuccessful();
		mSqLiteDatabase.endTransaction();
	};

	public void delete(String whereClause, String... whereArgs) {
		mSqLiteDatabase.beginTransaction();

		mSqLiteDatabase.delete(mTableName, whereClause, whereArgs);

		mSqLiteDatabase.setTransactionSuccessful();
		mSqLiteDatabase.endTransaction();
	};


	/**
	 * 查询所有数据
	 * @return 数据表中，每一行数据( 按照从左到右的循序保存为一个String数组 ) 组成的ArrayList<String []>;
	 */
	public ArrayList<String[]> selectAll() {

		ArrayList<String[]> list = new ArrayList<String[]>();

		Cursor c = mSqLiteDatabase.rawQuery("SELECT * FROM " + mTableName,
				new String[] {});

		while (c.moveToNext()) {

			String[] str = new String[c.getColumnCount()];

			for (int i = 0; i < c.getColumnCount(); i++) {
				str[i] = c.getString(i);
			}

			list.add(str);

		}
		c.close();
		return list;
	};



	/**
	 * 查询所有数据
	 * @return 数据表中，每一行的数据保存为一个hashmap 都转换为String类型
	 */
	public ArrayList<LinkedHashMap<String, String>> select() {

		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();

		Cursor c = mSqLiteDatabase.rawQuery("SELECT * FROM " + mTableName,
				new String[] {});

		while (c.moveToNext()) {

			LinkedHashMap<String, String> columnMap = new LinkedHashMap<String, String>();

			for (int i = 0; i < c.getColumnCount(); i++) {
				String key = c.getColumnName(i);
				String value = c.getString(i);
				columnMap.put(key, value);
			}

			list.add(columnMap);
		}
		c.close();
		return list;
	};

	/**
	 * 查询所有数据
	 *
	 * @return 数据表中，每一行的数据保存为一个hashmap 都转换为String类型
	 */
	public ArrayList<LinkedHashMap<String, String>> select(String clause,
			String[] args) {

		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();

		Cursor c = mSqLiteDatabase.rawQuery(clause, args);

		while (c.moveToNext()) {

			LinkedHashMap<String, String> columnMap = new LinkedHashMap<String, String>();

			for (int i = 0; i < c.getColumnCount(); i++) {
				String key = c.getColumnName(i);
				String value = c.getString(i);
				columnMap.put(key, value);
			}

			list.add(columnMap);
		}
		c.close();
		return list;
	};


	public LinkedHashMap<String, String> queryData(String queryClause, String[] args) {
		Cursor c = mSqLiteDatabase.rawQuery(queryClause, args);

		LinkedHashMap<String, String> columnMap = new LinkedHashMap<String, String>();

		while (c.moveToNext()) {

			for (int i = 0; i < c.getColumnCount();i++) {
				String key = c.getColumnName(i);
				String value = c.getString(i);
				columnMap.put(key, value);
			}

		}
		c.close();
		return columnMap;
	}

	public String getTableName() {
		return mTableName;
	}
}
