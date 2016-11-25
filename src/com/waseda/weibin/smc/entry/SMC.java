package com.waseda.weibin.smc.entry;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.waseda.weibin.smc.controller.ViewController;

public class SMC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ViewController().launch();
		
//		ExecuteShellCommand obj = new ExecuteShellCommand();
//		
//		String cmd = "verify inc.c";
//		obj.executeCommand(cmd);
		
//		Logger logger = LogManager.getRootLogger();
//		logger.debug("This is debug message.");

	}
}
