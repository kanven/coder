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

/**
 * 模版处理类
 * 
 * @author kanven
 *
 */
public class JavaCoderMarker extends AbstractTemplate {

	private static final String DEFAULT_JAVA_OUTPUT_PATH = Constants.DEFAULT_BASE_OUTPUT_PATH + File.separator + "java";

	private static final String JAVA_CONFIG_PATH = "conf/java.properties";

	/**
	 * java文件输出目录
	 */
	private String javaPath = DEFAULT_JAVA_OUTPUT_PATH;

	public JavaCoderMarker() {

	}

	public JavaCoderMarker(String javaPath) {
		this.javaPath = javaPath;
	}

	public JavaCoderMarker(String tmplPath, String javaPath) {
		super(Constants.DEFAULT_CHARSET, tmplPath);
		this.javaPath = javaPath;
	}

	public JavaCoderMarker(String encode, String tmplPath, String javaPath) {
		super(encode, tmplPath);
		this.javaPath = javaPath;
	}

	@Override
	public Writer createWriter(URL context, Object o) throws IOException {
		if (!(o instanceof EntityMeta)) {
			throw new IllegalStateException("模版元数据类型错误,不是【" + EntityMeta.class.getSimpleName() + "】类型");
		}
		EntityMeta meta = (EntityMeta) o;
		StringBuilder sb = new StringBuilder(DEFAULT_JAVA_OUTPUT_PATH);
		if (StringUtils.isNotBlank(javaPath)) {
			String jp = javaPath.replaceAll("/|\\\\", File.separator);
			if (jp.endsWith(File.separator)) {
				jp = jp.substring(0, jp.length() - 1);
			}
			sb.append(File.separator).append(jp);
		}
		sb.append(File.separator);
		String path = sb.toString();
		URL out = ClassLoader.getSystemResource(path);
		try {
			if (out == null) {
				out = new URL(context, path);
				File jf = new File(out.toURI());
				jf.mkdirs();
			}
			String pkg = meta.getPkg();
			if (StringUtils.isNotBlank(pkg)) {
				pkg = pkg.replaceAll("\\.", File.separator);
			}
			URL pu = new URL(out, pkg + File.separator);
			File pd = new File(pu.toURI());
			if (!pd.exists()) {
				pd.mkdirs();
			}
			String clazz = meta.getClazz();
			URL ju = new URL(pu, clazz + ".java");
			File java = new File(ju.toURI());
			if (java.exists()) {
				java.delete();
			}
			java.createNewFile();
			return new BufferedWriter(new FileWriter(java));
		} catch (URISyntaxException e) {
			throw new IllegalStateException("文件处理出现异常！", e);
		}
	}

	public String getJavaPath() {
		return javaPath;
	}

	public void setJavaPath(String javaPath) {
		this.javaPath = javaPath;
	}

	@Override
	public String getTmpl() {
		try {
			return PropertiesUtils.getProperty(JAVA_CONFIG_PATH, "tmpl.name");
		} catch (URISyntaxException | IOException e) {
			throw new IllegalStateException("获取指定模版文件名称出现！", e);
		}
	}

}
