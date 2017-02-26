package com.kanven.tools.code.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanven.tools.code.EntityMeta;
import com.kanven.tools.code.FieldMeta;
import com.kanven.tools.code.TypeConvert;

/**
 * 
 * @author kanven
 *
 */
public abstract class TableHandler implements Handler {

	private static final Logger log = LoggerFactory.getLogger(TableHandler.class);

	private String user;

	private String password;

	private String url;

	protected abstract void loadDriver() throws ClassNotFoundException;

	public TableHandler() {

	}

	public TableHandler(String url, String user, String password) throws ClassNotFoundException {
		this.url = url;
		this.user = user;
		this.password = password;
		loadDriver();
	}

	private Connection getConn() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public List<String> getTables() throws SQLException {
		List<String> tables = new LinkedList<String>();
		DatabaseMetaData meta = getConn().getMetaData();
		String[] types = { "TABLE" };
		ResultSet rs = meta.getTables(null, null, null, types);
		while (rs.next()) {
			String table = rs.getString(TableDesc.NAME.value());
			tables.add(table);
		}
		return tables;
	}

	public List<FieldMeta> getColumns(String table) throws SQLException {
		List<FieldMeta> columns = new LinkedList<FieldMeta>();
		if (StringUtils.isNotEmpty(table)) {
			Connection conn = getConn();
			DatabaseMetaData meta = conn.getMetaData();
			FieldMeta key = getPrimaryKey(table);
			ResultSet rs = meta.getColumns(null, null, table, null);
			while (rs.next()) {
				FieldMeta fm = buildField(rs);
				if (key != null && key.getColumn().equals(fm.getColumn())) {
					fm.setPrimaryKey(true);
				}
				columns.add(fm);
			}
		}
		return columns;
	}

	public FieldMeta getPrimaryKey(String table) throws SQLException {
		Connection conn = getConn();
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet rs = meta.getPrimaryKeys(null, null, table);
		FieldMeta fm = null;
		if (!rs.isAfterLast()) {
			rs.next();
			fm = new FieldMeta();
			String column = rs.getString(ColumnDesc.NAME.value());
			fm.setColumn(column);
			fm.setField(toHump(column));
			fm.setPrimaryKey(true);
		}
		return fm;
	}

	public EntityMeta buildEntity(String table, String pkg) {
		if (StringUtils.isEmpty(table)) {
			return null;
		}
		try {
			EntityMeta meta = new EntityMeta(pkg);
			meta.setTable(table);
			meta.setClazz(toClazz(table));
			meta.setFields(getColumns(table));
			meta.setPrimaryKey(getPrimaryKey(table));
			return meta;
		} catch (SQLException e) {
			log.error("获取数据库表列信息出现异常！", e);
			return null;
		}
	}

	public List<EntityMeta> buildEntities(String pkg) {
		List<EntityMeta> entities = new ArrayList<EntityMeta>();
		try {
			List<String> tables = getTables();
			for (String table : tables) {
				if (StringUtils.isEmpty(table)) {
					continue;
				}
				EntityMeta entity = buildEntity(table, pkg);
				if (entity != null) {
					entities.add(entity);
				}
			}
		} catch (SQLException e) {
			log.error("获取数据库表信息出现异常！", e);
		}
		return entities;
	}

	private FieldMeta buildField(ResultSet rs) throws SQLException {
		FieldMeta fm = new FieldMeta();
		String column = rs.getString(ColumnDesc.NAME.value());
		fm.setColumn(column);
		fm.setField(toHump(column));
		fm.setComment(rs.getString(ColumnDesc.REMARKS.value()));
		fm.setDbType(rs.getString(ColumnDesc.DB_TYPE.value()));
		fm.setJavaType(TypeConvert.convert(rs.getInt(ColumnDesc.JAVA_TYPE.value())));
		return fm;
	}

	/**
	 * 数据库字段下划线转换成驼峰
	 * 
	 * @param column
	 *            数据库字段
	 * @return
	 */
	private String toHump(String column) {
		if (StringUtils.isEmpty(column) || !column.contains("_")) {
			return column;
		}
		String[] fields = column.split("_");
		StringBuilder sb = new StringBuilder(fields[0]);
		for (int i = 1, len = fields.length; i < len; i++) {
			String field = fields[i];
			sb.append(firstCharUpper(field));
		}
		return sb.toString();
	}

	private String firstCharUpper(String field) {
		char first = field.charAt(0);
		if (Character.isUpperCase(first)) {
			return field;
		}
		StringBuilder sb = new StringBuilder();
		first = Character.toUpperCase(first);
		sb.append(first).append(field.substring(1));
		return sb.toString();
	}

	private String toClazz(String clazz) {
		if (StringUtils.isEmpty(clazz)) {
			return clazz;
		}
		StringBuilder sb = new StringBuilder();
		String[] items = clazz.split("_");
		for (String item : items) {
			sb.append(firstCharUpper(item));
		}
		return sb.toString();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
