package com.kanven.tools.code.tmpl;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public abstract class AbstractTemplate<T> implements Template<T> {

	static final String DEFAULT_ENCODE = "UTF-8";

	static final String DEFAULT_BASE_PACKAGE_PATH = "tmpl";

	static final String DEFAULT_BASE_OUTPUT_PATH = "output";

	protected String encode = DEFAULT_ENCODE;

	protected String tmplPath = DEFAULT_BASE_PACKAGE_PATH;

	protected Configuration cfg;

	private boolean inited = false;

	private Lock lock = new ReentrantLock();

	public AbstractTemplate() {

	}

	public AbstractTemplate(String encode, String tmplPath) {
		this.encode = encode;
		this.tmplPath = tmplPath;
	}

	public void process(T meta)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException {
		init();
		URL context = ClassLoader.getSystemResource("");
		URL tpls = ClassLoader.getSystemResource(tmplPath);
		File tmplDir = null;
		try {
			tmplDir = new File(tpls.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException("【" + tmplPath + "】模版文件不存在！", e);
		}
		if (tmplDir.exists() && tmplDir.isDirectory()) {
			File[] fiels = tmplDir.listFiles();
			if (fiels != null && fiels.length > 0) {
				for (File file : fiels) {
					String name = file.getName();
					freemarker.template.Template tmpl;
					try {
						tmpl = cfg.getTemplate(name);
						Writer writer = createWriter(context, file, meta);
						if (writer != null) {
							try {
								tmpl.process(meta, writer);
							} finally {
								writer.close();
							}
							continue;
						}
						throw new IllegalStateException("【" + name + "】文件生成失败！");
					} catch (IOException e) {
						throw new IllegalStateException("【" + name + "】文件生成失败！", e);
					}
				}
			}
		}
	}

	public abstract Writer createWriter(URL context, File fiel, T meta);

	protected void init() {
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

}
