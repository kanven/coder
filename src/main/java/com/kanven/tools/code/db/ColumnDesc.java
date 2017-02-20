package com.kanven.tools.code.db;

public enum ColumnDesc {

	NAME("COLUMN_NAME"), JAVA_TYPE("DATA_TYPE"), DB_TYPE("TYPE_NAME"), REMARKS("REMARKS");

	private String value;

	private ColumnDesc(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
