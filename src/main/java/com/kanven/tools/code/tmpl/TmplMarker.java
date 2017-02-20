package com.kanven.tools.code.tmpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import com.kanven.tools.code.EntityMeta;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

/**
 * 模版处理类
 * 
 * @author kanven
 *
 */
public class TmplMarker {

	private static final String DEFAULT_ENCODE = "UTF-8";

	private static final String DEFAULT_BASE_PACKAGE_PATH = "tmpl";

	private static final String DEFAULT_BASE_OUTPUT_PATH = "output";

	private static final String DEFAULT_JAVA_OUTPUT_PATH = DEFAULT_BASE_OUTPUT_PATH + File.separator + "java";

	private static final String DEFAULT_BATIES_OUTPUT_PATH = DEFAULT_BASE_OUTPUT_PATH + File.separator + "baties";

	private String encode = DEFAULT_ENCODE;

	private String tmplPath = DEFAULT_BASE_PACKAGE_PATH;

	private String javaPath;

	private String batiesPath;

	private Configuration cfg;

	private boolean inited = false;

	private Lock lock = new ReentrantLock();

	public TmplMarker() {

	}

	public TmplMarker(String javaPath, String batiesPath) {
		this.javaPath = javaPath;
		this.batiesPath = batiesPath;
	}

	public TmplMarker(String tmplPath, String javaPath, String batiesPath) {
		this.tmplPath = tmplPath;
		this.javaPath = javaPath;
		this.batiesPath = batiesPath;
	}

	public TmplMarker(String encode, String tmplPath, String javaPath, String batiesPath) {
		this.encode = encode;
		this.tmplPath = tmplPath;
		this.javaPath = javaPath;
		this.batiesPath = batiesPath;
	}

	public void process(EntityMeta meta) throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException, URISyntaxException {
		init();
		URL context = ClassLoader.getSystemResource("");
		URL tpls = ClassLoader.getSystemResource(tmplPath);
		File tmplDir = new File(tpls.toURI());
		if (tmplDir.exists() && tmplDir.isDirectory()) {
			File[] fiels = tmplDir.listFiles();
			if (fiels != null && fiels.length > 0) {
				for (File file : fiels) {
					String name = file.getName();
					Template template = cfg.getTemplate(name);
					Writer writer = null;
					if ("entity.ftlh".equals(name)) {
						writer = writeJavaFile(context, meta);
					} else if ("mybatis.ftlh".equals(name)) {
						writer = writerBatiesFile(context, meta);
					}
					if (writer != null) {
						try {
							template.process(meta, writer);
						} finally {
							writer.close();
						}
					}

				}
			}
		}
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

	private void init() {
		if (inited) {
			return;
		}
		lock.lock();
		try {
			if (!inited) {
				cfg = new Configuration(Configuration.VERSION_2_3_25);
				cfg.setDefaultEncoding(encode);
				cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
				cfg.setClassLoaderForTemplateLoading(ClassLoader.getSystemClassLoader(), tmplPath);
			}
		} finally {
			lock.unlock();
		}
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getTmplPath() {
		return tmplPath;
	}

	public void setTmplPath(String tmplPath) {
		this.tmplPath = tmplPath;
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
