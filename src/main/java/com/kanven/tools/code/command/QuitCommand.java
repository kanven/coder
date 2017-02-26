package com.kanven.tools.code.command;

import java.io.PrintWriter;

/**
 * 退出命令
 * 
 * @author kanven
 *
 */
public class QuitCommand implements Command {

	@Override
	public void execute(String[] args) {
		System.exit(0);
	}

	@Override
	public void setWriter(PrintWriter writer) {

	}

	@Override
	public void help() {

	}

	@Override
	public boolean hasOptions() {
		return false;
	}

}
