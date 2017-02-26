package com.kanven.tools.code.command.gen;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;

import com.kanven.tools.code.command.AbstractCommand;
import com.kanven.tools.code.command.Command;
import com.kanven.tools.code.extension.ExtensionLoader;

/**
 * 代码生成命令
 * 
 * @author kanven
 *
 */
public class GenCommand extends AbstractCommand {

	private static final String DB = "db";

	private static final String DB_LONG_OPTION = "database";

	private static final String PASSWORD = "p";

	private static final String PASSWORD_LONG_OPTION = "password";

	private static final String PACKAGE = "pkg";

	private static final String PACKAGE_LONG_OPTION = "package";

	private static final String DIR = "dir";

	private static final String DIR_LONG_OPTION = "baseDir";

	private static final String TYPE = "t";

	private static final String TYPE_LNG_OPTION = "type";

	private static final String HELP = "h";

	private static final String HELP_LONG_OPTION = "help";

	@Override
	protected void initOptions() {
		options.addOption(
				Option.builder().argName(DB).longOpt(DB_LONG_OPTION).desc("数据库连接地址").hasArg(true).required().build());
		options.addOption(Option.builder().argName(PASSWORD).longOpt(PASSWORD_LONG_OPTION).desc("数据库密码").hasArg(true)
				.required().build());
		options.addOption(
				Option.builder().argName(PACKAGE).longOpt(PACKAGE_LONG_OPTION).desc("包名").hasArg(true).build());
		options.addOption(Option.builder().argName(DIR).longOpt(DIR_LONG_OPTION).desc("生成文件存放目录").hasArg(true).build());
		options.addOption(Option.builder().argName(TYPE).longOpt(TYPE_LNG_OPTION).desc("文件类型（java或者mybaties）").build());
		options.addOption(Option.builder().argName(HELP).longOpt(HELP_LONG_OPTION).desc("命令帮助文档").build());
	}

	@Override
	protected void doParse(CommandLine cmd) {
		if (cmd.hasOption(HELP)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("gen", options);
			return;
		}
		List<String> args = cmd.getArgList();
		if (args == null) {
			return;
		}
		Argument argument = new Argument();
		for (String arg : args) {
			String value = cmd.getOptionValue(arg);
			switch (arg) {
			case DB:
				argument.setUrl(value);
				break;
			case PASSWORD:
				argument.setPassword(value);
				break;
			case PACKAGE:
				argument.setPkg(value);
			case DIR:
				argument.setBaseDir(value);
			case TYPE:
				argument.setType(value);
			case HELP:
				break;
			default:
				break;
			}
		}
		doExacute(cmd, argument);
	}

	private void doExacute(CommandLine cmd, Argument argument) {
		ExtensionLoader<Command> loader = ExtensionLoader.getExtensionLoader(Command.class);
		Command command = loader.getEntity(argument.getDbKind());
	}

}
