package com.waseda.weibin.smc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static boolean checkFileNames(String input) {
		String str = input.trim() + " ";
		// Match inputs like "a.c b.c val1 val2".
//		Pattern p = Pattern.compile("((\\w)+\\.c\\s)+((\\w)+\\s)+");
		
		// Match inputs like "a.c b.c ..."
		Pattern p = Pattern.compile("((\\w)+\\.c\\s)+");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static boolean checkLTLs() {
		
		return false;
	}
	
}
