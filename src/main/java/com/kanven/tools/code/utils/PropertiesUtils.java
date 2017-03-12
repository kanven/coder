package com.kanven.tools.code.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class PropertiesUtils {

	public static String getProperty(String path, String key) throws URISyntaxException, IOException {
		InputStream in = ClassLoader.getSystemResourceAsStream(path);
		Properties properties = new Properties();
		properties.load(in);
		return properties.getProperty(key);
	}

}
