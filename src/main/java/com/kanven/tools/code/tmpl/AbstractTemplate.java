package com.kanven.tools.code.tmpl;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.kanven.tools.code.Constants;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public abstract class AbstractTemplate implements Template {

	protected String encode = Constants.DEFAULT_CHARSET;

	/**
	 * 模版存放路径
	 */
	protected String tmplPath = Constants.DEFAULT_TEMPLATE_PATH;

	protected Configuration cfg;

	private boolean inited = false;

	private Lock lock = new ReentrantLock();

	public AbstractTemplate() {

	}

	public AbstractTemplate(String tmplPath) {
		this.tmplPath = tmplPath;
	}

	public AbstractTemplate(String encode, String tmplPath) {
		this.encode = encode;
		this.tmplPath = tmplPath;
	}

	@Override
	public void process(Object meta)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException {
		if (meta == null) {
			throw new IllegalStateException("没有模版元数据！");
		}
		init();
		URL context = ClassLoader.getSystemResource("");
		URL tpls = ClassLoader.getSystemResource(tmplPath);
		File tmplDir = null;
		try {
			tmplDir = new File(tpls.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException("【" + tmplPath + "】模版文件夹不存在！", e);
		}
		if (tmplDir.exists() && tmplDir.isDirectory()) {
			String fp = getTmpl();
			if (StringUtils.isBlank(fp)) {
				throw new IllegalStateException("没有制定模版文件！");
			}
			String path = tmplPath + fp;
			URL url = ClassLoader.getSystemResource(path);
			File file = null;
			try {
				file = new File(url.toURI());
			} catch (URISyntaxException e) {
				throw new IllegalStateException("模版文件获取失败！", e);
			}
			String name = file.getName();
			Writer writer = null;
			try {
				writer = createWriter(context, meta);
				if (writer != null) {
					freemarker.template.Template tmpl = cfg.getTemplate(name);
					tmpl.process(meta, writer);
				}
			} catch (IOException e) {
				throw new IllegalStateException("【" + name + "】文件生成失败！", e);
			} finally {
				IOUtils.closeQuietly(writer);
			}
		}
		throw new IllegalStateException(tmplPath + "路径不存在或不是文件夹！");
	}

	public abstract String getTmpl();

	public abstract Writer createWriter(URL context, Object meta) throws IOException;

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
