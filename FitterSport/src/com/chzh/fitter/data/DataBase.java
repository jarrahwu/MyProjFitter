package com.chzh.fitter.data;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DataBase  implements IDataBase{

	private SQLiteDatabase mSqLiteDatabase;

	private Table mTable;

	public DataBase(SQLiteDatabase sqliteDatabase) {
		mSqLiteDatabase = sqliteDatabase;
	}

	@Override
	public Table getTable(String tableName) {

		if (isTableExist(tableName)) {

			if (mTable == null) {
				mTable = new Table(mSqLiteDatabase,tableName);
			}

			return mTable;

		}else {
			Log.e(getClass().getName(), "TABLE NOT EXIST!!");
			return null;
		}


	}

	public void execSql(String sql) {
		try{
			mSqLiteDatabase.execSQL(sql);
		}catch(SQLException e){
			Log.e(this.getClass().getName(), e.toString());
		}
	}


	/**
	 * 创建表
	 * @param tableName
	 * @param columnClauses
	 */
	public void createTable(String tableName, String... columnClauses) {
		String sql = getCreateTableClause(tableName, columnClauses);
		execSql(sql);
	}

    /**
     * 获取创建表的表达式,
     * @return
     */
    private String getCreateTableClause(String tableName, String... columnClause) {
    	StringBuffer sb = new StringBuffer();
    	//表名
    	sb.append("CREATE TABLE " + tableName);

    	sb.append("(");//表结构表达式的开始

    	for (int i = 0; i < columnClause.length; i++) {
    		String clause = columnClause[i];
			sb.append(clause);

			//如果不是最后一个表达式就添加一个", "
			if (i != columnClause.length - 1) {
				sb.append(", ");
			}
		}
    	sb.append(")");//表结构表达式的结束
    	Log.e("TAG : sql >>>", sb.toString());
    	return sb.toString();
    }

	/**
	 * 判断某张表是否存在
	 * @param tabName 表名
	 * @return
	 */
    @Override
	public boolean isTableExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;

		try {

			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";

			cursor = mSqLiteDatabase.rawQuery(sql, null);

			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.toString());
		}
		return result;
	}

	@Override
	public void deleteTable(String tableName) {
		mSqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (mSqLiteDatabase != null) {
			mSqLiteDatabase.close();
		}
	}

}
