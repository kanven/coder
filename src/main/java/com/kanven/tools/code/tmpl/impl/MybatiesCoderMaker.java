package com.kanven.tools.code.tmpl.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.kanven.tools.code.Constants;
import com.kanven.tools.code.EntityMeta;
import com.kanven.tools.code.tmpl.AbstractTemplate;
import com.kanven.tools.code.utils.PropertiesUtils;

public class MybatiesCoderMaker extends AbstractTemplate {

	private static final String DEFAULT_BATIES_OUTPUT_PATH = Constants.DEFAULT_BASE_OUTPUT_PATH + File.separator
			+ "baties";

	private static final String MYBATIES_CONFIG_PATH = "conf/mybaties.properties";

	/**
	 * mybatis文件输出目录
	 */
	private String batiesPath = DEFAULT_BATIES_OUTPUT_PATH;

	@Override
	public Writer createWriter(URL context, Object o) throws IOException {
		if (!(o instanceof EntityMeta)) {
			throw new IllegalStateException("模版元数据类型错误,不是【" + EntityMeta.class.getSimpleName() + "】类型");
		}
		EntityMeta meta = (EntityMeta) o;
		StringBuilder sb = new StringBuilder(DEFAULT_BATIES_OUTPUT_PATH);
		if (StringUtils.isNotBlank(batiesPath)) {
			String bp = batiesPath.replaceAll("/|\\\\", File.separator);
			if (bp.endsWith(File.separator)) {
				bp = bp.substring(0, bp.length() - 1);
			}
			sb.append(File.separator).append(bp);
		}
		sb.append(File.separator);
		String path = sb.toString();

		try {
			URL out = ClassLoader.getSystemResource(path);
			if (out == null) {
				out = new URL(context, path);
				File bf = new File(out.toURI());
				bf.mkdirs();
			}
			URL bu = new URL(out, meta.getTable() + "_mapper.xml");
			File xf = new File(bu.toURI());
			if (xf.exists()) {
				xf.delete();
			}
			xf.createNewFile();
			return new BufferedWriter(new FileWriter(xf));
		} catch (URISyntaxException e) {
			throw new IllegalStateException("文件处理出现异常！", e);
		}
	}

	public String getBatiesPath() {
		return batiesPath;
	}

	public void setBatiesPath(String batiesPath) {
		this.batiesPath = batiesPath;
	}

	@Override
	public String getTmpl() {
		try {
			return PropertiesUtils.getProperty(MYBATIES_CONFIG_PATH, "tmpl.name");
		} catch (URISyntaxException | IOException e) {
			throw new IllegalStateException("获取指定模版文件名称出现！", e);
		}
	}

}
