package com.waseda.weibin.smc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author  Weibin Luo
 * @version Created on Nov 25, 2016 3:31:39 PM
 */
public class Command {

	public static void execute(String command) {
			
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
		System.out.println(output);
	}
}
