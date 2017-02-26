package com.kanven.tools.code.db;

import java.sql.SQLException;
import java.util.List;

import com.kanven.tools.code.EntityMeta;
import com.kanven.tools.code.FieldMeta;

public interface Handler {

	public List<String> getTables() throws SQLException;

	public List<FieldMeta> getColumns(String table) throws SQLException;

	public FieldMeta getPrimaryKey(String table) throws SQLException;

	public EntityMeta buildEntity(String table, String pkg);

	public List<EntityMeta> buildEntities(String pkg);

	public void setUser(String user);

	public void setPassword(String password);

	public void setUrl(String url);

}
