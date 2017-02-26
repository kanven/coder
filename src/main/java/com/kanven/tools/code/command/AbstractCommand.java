package com.kanven.tools.code.command;

import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 命令抽象类
 * 
 * @author kanven
 *
 */
public abstract class AbstractCommand implements Command {

	private PrintWriter writer;

	protected Options options = new Options();

	protected CommandLineParser parser = new DefaultParser();

	public AbstractCommand() {
		initOptions();
	}

	protected abstract void initOptions();

	@Override
	public void execute(String[] args) {
		try {
			CommandLine cmd = parser.parse(options, args);
			doParse(cmd);
		} catch (ParseException e) {

		}
	}

	public PrintWriter getWriter() {
		return writer;
	}

	@Override
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	protected abstract void doParse(CommandLine cmd);

}
