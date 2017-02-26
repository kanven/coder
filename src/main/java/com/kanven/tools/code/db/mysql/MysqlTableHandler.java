package com.kanven.tools.code.db.mysql;

import com.kanven.tools.code.db.TableHandler;
import com.mysql.jdbc.Driver;

public class MysqlTableHandler extends TableHandler {

	public MysqlTableHandler() {

	}

	public MysqlTableHandler(String url, String user, String password) throws ClassNotFoundException {
		super(url, user, password);
	}

	@Override
	protected void loadDriver() throws ClassNotFoundException {
		String driver = Driver.class.getName();
		Class.forName(driver);
	}

}
