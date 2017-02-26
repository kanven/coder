package com.kanven.tools.code.command.gen;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

/**
 * 代码生成命令实体
 * 
 * @author kanven
 *
 */
public class Argument {

	private String url;

	private String dbKind;

	private String user;

	private String password;

	private String table;

	private String pkg;

	private String baseDir;

	private String type;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		if (StringUtils.isNotBlank(url)) {
			int index = url.indexOf(":");
			if (index > -1) {
				int start = index + 1;
				int end = url.indexOf(":", start);
				if (end == -1) {
					end = url.length();
				}
				this.dbKind = url.substring(start, end);
			}
		}
	}

	public String getDbKind() {
		return dbKind;
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

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		if (StringUtils.isBlank(baseDir)) {
			return;
		}
		if (baseDir.startsWith("/|\\\\")) {
			baseDir = baseDir.substring(1);
		}
		if (!baseDir.endsWith("/|\\\\")) {
			baseDir += File.separator;
		}
		this.baseDir = baseDir;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Argument [url=" + url + ", dbKind=" + dbKind + ", password=" + password + ", pkg=" + pkg + ", baseDir="
				+ baseDir + ", type=" + type + "]";
	}

}
