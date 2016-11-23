package com.waseda.weibin.smc.entry;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SMC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ExecuteShellCommand obj = new ExecuteShellCommand();
//		
//		String cmd = "verify inc.c";
//		obj.executeCommand(cmd);
		
		Logger logger = LogManager.getRootLogger();
		logger.debug("This is debug message.");

	}
	
	private void executeCommand(String command) {
		
		StringBuffer output = new StringBuffer();
		
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		return output.toString();
		System.out.println(output);
	}
}
