package com.kanven.tools.code;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jline.ConsoleReader;

public class Coder {

	private static final Logger log = LoggerFactory.getLogger(Coder.class);

	public static void main(String[] args) {
		ConsoleReader reader = null;
		PrintWriter out = new PrintWriter(System.out);
		Invoker invoker = new Invoker(out);
		try {
			reader = new ConsoleReader();
			while (true) {
				String cmd = reader.readLine("coder>");
				if (StringUtils.isBlank(cmd)) {
					out.println("请输入命令");
					out.flush();
				} else {
					invoker.invoke(cmd);
				}
			}
		} catch (IOException e) {
			String msg = "命令执行出现异常";
			log.error(msg, e);
			out.println(msg + ",具体错误信息请看 错误日志！");
			out.close();
			System.exit(-1);
		}
	}
}
