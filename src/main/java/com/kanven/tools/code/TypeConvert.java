package com.kanven.tools.code;

import java.sql.Types;
import java.util.Date;

/**
 * 类型转换
 * 
 * @author kanven
 *
 */
public class TypeConvert {

	public static String convert(int num) {
		switch (num) {
		case Types.TIMESTAMP:
		case Types.TIME:
		case Types.DATE:
			return Date.class.getName();
		case Types.BOOLEAN:
			return Boolean.class.getSimpleName();
		case Types.BIT:
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
			return Integer.class.getSimpleName();
		case Types.BIGINT:
			return Long.class.getSimpleName();
		case Types.REAL:
		case Types.FLOAT:
			return Float.class.getSimpleName();
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.NUMERIC:
			return Double.class.getSimpleName();
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.BLOB:
			return String.class.getSimpleName();
		default:
			throw new RuntimeException(num + ",不在转换类型范围之内！");
		}
	}

}
