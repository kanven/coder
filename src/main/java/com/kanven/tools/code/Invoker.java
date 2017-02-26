package com.kanven.tools.code;

import java.io.PrintWriter;
import java.util.Arrays;

import com.kanven.tools.code.command.Command;
import com.kanven.tools.code.extension.ExtensionLoader;

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
		command.setWriter(print);
		command.execute(Arrays.copyOfRange(params, 1, params.length - 1));
	}

	public void setPrint(PrintWriter print) {
		this.print = print;
	}

}
