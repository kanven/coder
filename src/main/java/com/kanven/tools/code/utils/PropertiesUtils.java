package com.kanven.tools.code.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class PropertiesUtils {

	private static final String CLASSPATH = "classpath:";

	public static String getProperty(String path, String key) throws URISyntaxException, IOException {
		File file = null;
		if (path.startsWith(CLASSPATH)) {
			path = path.substring(CLASSPATH.length());
			if (path.startsWith("/|\\\\")) {
				path = path.substring(1);
			}
			URL url = ClassLoader.getSystemResource(path);
			file = new File(url.toURI());
		} else {
			file = new File(path);
		}
		if (!file.exists() || file.isFile()) {
			throw new FileNotFoundException(path + "文件不存在！");
		}
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));
		return properties.getProperty(key);
	}

}
