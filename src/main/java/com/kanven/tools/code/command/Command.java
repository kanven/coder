package com.kanven.tools.code.command;

import java.io.PrintWriter;

/**
 * 命令接口
 * 
 * @author kanven
 *
 */
public interface Command {

	public void execute(String[] args);

	public void setWriter(PrintWriter writer);

}
