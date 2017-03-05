package com.kanven.tools.code.tmpl;

import java.io.IOException;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * 
 * 模版接口
 * 
 * @author kanven
 *
 * @param
 */
public interface Template {

	/**
	 * 模版处理方法
	 * 
	 * @param meta
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void process(Object meta)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException;

}
