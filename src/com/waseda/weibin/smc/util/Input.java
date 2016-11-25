package com.waseda.weibin.smc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author  Weibin Luo
 * @version Created on Nov 25, 2016 3:34:12 PM
 */
public class Input {

	public static String getInput() {
		
		String input = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			input = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return input;
	}
	
	public boolean checkInput(String input) {
		
		return false;
		
	}
}
