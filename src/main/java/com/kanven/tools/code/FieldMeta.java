package com.kanven.tools.code;

/**
 * 字段元数据实体类
 * 
 * @author kanven
 *
 */
public class FieldMeta {

	private String field;

	private String column;

	private String javaType;

	private String dbType;

	private String comment;

	private boolean primaryKey = false;

	public FieldMeta() {

	}

	public void setField(String field) {
		this.field = field;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getField() {
		return field;
	}

	public String getColumn() {
		return column;
	}

	public String getJavaType() {
		return javaType;
	}

	public String getDbType() {
		return dbType;
	}

	public String getComment() {
		return comment;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	@Override
	public String toString() {
		return "FieldMeta [field=" + field + ", column=" + column + ", javaType=" + javaType + ", dbType=" + dbType
				+ ", comment=" + comment + ", primaryKey=" + primaryKey + "]";
	}

}
