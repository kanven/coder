package com.kanven.tools.code;

import java.io.PrintWriter;
import java.util.Arrays;

import com.kanven.tools.code.command.Command;
import com.kanven.tools.code.extension.ExtensionLoader;

/**
 * 命令调用者
 * 
 * @author kanven
 *
 */
public class Invoker {

	private ExtensionLoader<Command> loader = ExtensionLoader.getExtensionLoader(Command.class);

	private PrintWriter print;

	public Invoker() {

	}

	public Invoker(PrintWriter print) {
		this.print = print;
	}

	public void invoke(String cmd) {
		String[] params = cmd.split(" ");
		String name = params[0];
		Command command = loader.getEntity(name);
		if (command == null) {
			throw new IllegalStateException("【" + cmd + "】命令对应实体不存在！");
		}
		command.setWriter(print);
		if (params.length > 1) {
			command.execute(Arrays.copyOfRange(params, 1, params.length - 1));
		} else {
			if (command.hasOptions()) {
				command.help();
			} else {
				command.execute(null);
			}
		}
	}

	public void setPrint(PrintWriter print) {
		this.print = print;
	}

}
