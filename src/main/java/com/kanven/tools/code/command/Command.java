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

	/**
	 * 命令帮助信息
	 */
	public void help();

	/**
	 * 是否有命令可选性
	 */
	public boolean hasOptions();

}
