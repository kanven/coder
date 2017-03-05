package com.kanven.tools.code.command.gen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;

import com.kanven.tools.code.EntityMeta;
import com.kanven.tools.code.command.AbstractCommand;
import com.kanven.tools.code.db.Handler;
import com.kanven.tools.code.extension.ExtensionLoader;
import com.kanven.tools.code.tmpl.Template;
import com.kanven.tools.code.tmpl.impl.JavaCoderMarker;

import freemarker.template.TemplateException;

/**
 * 代码生成命令
 * 
 * @author kanven
 *
 */
public class GenCommand extends AbstractCommand {

	private static final String DB = "db";

	private static final String DB_LONG_OPTION = "database";

	private static final String USER = "u";

	private static final String USER_LONG_OPTION = "user";

	private static final String PASSWORD = "p";

	private static final String PASSWORD_LONG_OPTION = "password";

	private static final String PACKAGE = "pkg";

	private static final String PACKAGE_LONG_OPTION = "package";

	private static final String DIR = "dir";

	private static final String DIR_LONG_OPTION = "baseDir";

	private static final String TYPE = "t";

	private static final String TYPE_LNG_OPTION = "type";

	private static final String TABLE = "tb";

	private static final String TABLE_LONG_OPTION = "table";

	private static final String HELP = "h";

	private static final String HELP_LONG_OPTION = "help";

	@Override
	protected void initOptions() {
		options.addOption(Option.builder(DB).longOpt(DB_LONG_OPTION).desc("数据库连接地址").hasArg(true).build());
		options.addOption(Option.builder(USER).longOpt(USER_LONG_OPTION).desc("数据库用户名").hasArg(true).build());
		options.addOption(Option.builder(PASSWORD).longOpt(PASSWORD_LONG_OPTION).desc("数据库密码").hasArg(true).build());
		options.addOption(Option.builder(TABLE).longOpt(TABLE_LONG_OPTION).desc("数据库表名").hasArg(true).build());
		options.addOption(Option.builder(PACKAGE).longOpt(PACKAGE_LONG_OPTION).desc("包名").hasArg(true).build());
		options.addOption(Option.builder(DIR).longOpt(DIR_LONG_OPTION).desc("生成文件存放目录").hasArg(true).build());
		options.addOption(
				Option.builder(TYPE).longOpt(TYPE_LNG_OPTION).hasArg(true).desc("文件类型（java或者mybaties）").build());
		options.addOption(Option.builder(HELP).longOpt(HELP_LONG_OPTION).desc("命令帮助文档").build());
	}

	@Override
	protected void doParse(CommandLine cmd) {
		if (cmd.hasOption(HELP)) {
			help();
			return;
		}
		if ((!cmd.hasOption(DB) && !cmd.hasOption(DB_LONG_OPTION)) || StringUtils.isBlank(cmd.getOptionValue(DB))) {
			PrintWriter print = getWriter();
			print.println("数据库连接地址不能为空！");
			print.flush();
			return;
		}
		if ((!cmd.hasOption(PASSWORD) && !cmd.hasOption(PASSWORD_LONG_OPTION))
				|| StringUtils.isBlank(cmd.getOptionValue(PASSWORD))) {
			PrintWriter print = getWriter();
			print.println("数据库密码不能为空！");
			print.flush();
			return;
		}
		Argument argument = new Argument();
		Iterator<Option> iterator = cmd.iterator();
		while (iterator.hasNext()) {
			Option option = iterator.next();
			buildArgument(option, argument);
		}
		doExacute(cmd, argument);
	}

	public void buildArgument(Option option, Argument argument) {
		String opt = option.getOpt();
		String value = option.getValue();
		switch (opt) {
		case DB:
		case DB_LONG_OPTION:
			argument.setUrl(value);
			break;
		case USER:
		case USER_LONG_OPTION:
			argument.setUser(value);
		case PASSWORD:
		case PASSWORD_LONG_OPTION:
			argument.setPassword(value);
			break;
		case TABLE:
		case TABLE_LONG_OPTION:
			argument.setTable(value);
		case PACKAGE:
		case PACKAGE_LONG_OPTION:
			argument.setPkg(value);
		case DIR:
		case DIR_LONG_OPTION:
			argument.setBaseDir(value);
			break;
		case TYPE:
		case TYPE_LNG_OPTION:
			argument.setType(value);
		default:
			break;
		}
	}

	private void doExacute(CommandLine cmd, Argument argument) {
		ExtensionLoader<Handler> loader = ExtensionLoader.getExtensionLoader(Handler.class);
		Handler handler = loader.getEntity(argument.getDbKind());
		if (handler == null) {
			return;
		}
		handler.setUrl(argument.getUrl());
		handler.setPassword(argument.getPassword());
		handler.setUser(argument.getUser());
		if (StringUtils.isNotBlank(argument.getType())) {
			ExtensionLoader<Template> tmplLoader = ExtensionLoader.getExtensionLoader(Template.class);
		}
		JavaCoderMarker marker = new JavaCoderMarker();
		if (StringUtils.isBlank(argument.getTable())) {
			List<EntityMeta> metas = handler.buildEntities(argument.getPkg());
			if (metas == null || metas.size() <= 0) {
				return;
			}
			for (EntityMeta meta : metas) {
				try {
					marker.process(meta);
				} catch (IOException | TemplateException e) {
					throw new IllegalStateException("生成文件出现异常！", e);
				}
			}
		} else {
			EntityMeta meta = handler.buildEntity(argument.getTable(), argument.getPkg());
			try {
				marker.process(meta);
			} catch (IOException | TemplateException e) {
				throw new IllegalStateException("生成文件出现异常！", e);
			}
		}
	}

	@Override
	public void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("gen", options);
	}

	@Override
	public boolean hasOptions() {
		return true;
	}

}
