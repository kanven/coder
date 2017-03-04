package com.kanven.tools.code.tmpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.kanven.tools.code.EntityMeta;

/**
 * 模版处理类
 * 
 * @author kanven
 *
 */
public class JavaCoderlMarker extends AbstractTemplate<EntityMeta> {

	private static final String DEFAULT_JAVA_OUTPUT_PATH = DEFAULT_BASE_OUTPUT_PATH + File.separator + "java";

	private static final String DEFAULT_BATIES_OUTPUT_PATH = DEFAULT_BASE_OUTPUT_PATH + File.separator + "baties";

	private String javaPath;

	private String batiesPath;

	public JavaCoderlMarker() {

	}

	public JavaCoderlMarker(String javaPath, String batiesPath) {
		this.javaPath = javaPath;
		this.batiesPath = batiesPath;
	}

	public JavaCoderlMarker(String tmplPath, String javaPath, String batiesPath) {
		super(DEFAULT_ENCODE, tmplPath);
		this.javaPath = javaPath;
		this.batiesPath = batiesPath;
	}

	public JavaCoderlMarker(String encode, String tmplPath, String javaPath, String batiesPath) {
		super(encode, tmplPath);
		this.javaPath = javaPath;
		this.batiesPath = batiesPath;
	}

	@Override
	public Writer createWriter(URL context, File file, EntityMeta meta) {
		String name = file.getName();
		Writer writer = null;
		try {
			if ("entity.ftlh".equals(file.getName())) {
				writer = writeJavaFile(context, meta);
			} else if ("mybatis.ftlh".equals(name)) {
				writer = writerBatiesFile(context, meta);
			}
		} catch (URISyntaxException | IOException e) {
			throw new IllegalStateException("【" + name + "】文件生成失败！", e);
		}
		return writer;
	}

	private Writer writeJavaFile(URL context, EntityMeta meta) throws URISyntaxException, IOException {
		StringBuilder sb = new StringBuilder(DEFAULT_JAVA_OUTPUT_PATH);
		if (StringUtils.isNotEmpty(javaPath)) {
			String jp = javaPath.replaceAll("/|\\\\", File.separator);
			if (jp.endsWith(File.separator)) {
				jp = jp.substring(0, jp.length() - 1);
			}
			sb.append(File.separator).append(jp);
		}
		sb.append(File.separator);
		String path = sb.toString();
		URL out = ClassLoader.getSystemResource(path);
		if (out == null) {
			out = new URL(context, path);
			File jf = new File(out.toURI());
			jf.mkdirs();
		}
		String pkg = meta.getPkg();
		if (StringUtils.isNotEmpty(pkg)) {
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
	}

	private Writer writerBatiesFile(URL context, EntityMeta meta) throws URISyntaxException, IOException {
		StringBuilder sb = new StringBuilder(DEFAULT_BATIES_OUTPUT_PATH);
		if (StringUtils.isNotEmpty(javaPath)) {
			String bp = batiesPath.replaceAll("/|\\\\", File.separator);
			if (bp.endsWith(File.separator)) {
				bp = bp.substring(0, bp.length() - 1);
			}
			sb.append(File.separator).append(bp);
		}
		sb.append(File.separator);
		String path = sb.toString();
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
	}

	public String getJavaPath() {
		return javaPath;
	}

	public void setJavaPath(String javaPath) {
		this.javaPath = javaPath;
	}

	public String getBatiesPath() {
		return batiesPath;
	}

	public void setBatiesPath(String batiesPath) {
		this.batiesPath = batiesPath;
	}

}
