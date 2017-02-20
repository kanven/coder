package com.kanven.tools.code.db;

/**
 * 数据库表描述类型
 * 
 * @author kanven
 *
 */
public enum TableDesc {
	CAT("TABLE_CAT"), SCHEM("TABLE_SCHEM"), NAME("TABLE_NAME"), TYPE("TABLE_TYPE"), REMARKS("REMARKS");

	private String value;

	private TableDesc(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
