package com.chzh.fitter.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库帮助器
 * 用来创建数据库,关闭数据库
 *
 */
public class DataBaseHelper {

	protected Context mContext;

	private DataBase mDataBase;

	private SQLiteDatabase mSqLiteDatabase;

	public DataBaseHelper(Context context) {
		mContext = context;
	}

	/**
	 * 创建数据库，打开数据库
	 * @param name
	 * @return
	 */
	public DataBase createDB(String name) {
		mSqLiteDatabase = mContext.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
		mDataBase = new DataBase(mSqLiteDatabase);
		return mDataBase;
	}

	/**
	 * 获取数据库，没有的话就创建一个新的
	 * @param name
	 * @return
	 */
	public DataBase getDB(String name) {
		if (mDataBase == null) {
			return createDB(name);
		}
		return mDataBase;
	}

	public void closeDB() {
		if (mSqLiteDatabase != null) {
			mSqLiteDatabase.close();
		}
	}
}
