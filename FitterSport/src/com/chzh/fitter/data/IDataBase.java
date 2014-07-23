package com.chzh.fitter.data;

public interface IDataBase {

	/**
	 * 获取表 如果不存在返回null
	 * @param name
	 * @return
	 */
	Table getTable(String name);

	/**
	 * 删除表
	 * @param name
	 */
	void deleteTable(String name);

	/**
	 * 创建表
	 * @param tableName 表名
	 * @param columnClauses 每一列的数据表达式  例如："name VARCHAR(10)" "age SMALLINT"之类
	 * @return
	 */
	void createTable(String tableName, String... columnClauses);

	/**
	 * 判断表是否存在
	 * @param tableName
	 * @return
	 */
	boolean isTableExist(String tableName);
}
