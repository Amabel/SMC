package com.waseda.weibin.smc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  Weibin Luo
 * @version Created on Nov 28, 2016 9:44:25 AM
 */
public class RegularExpression {

	
	public static String findNumberInString(String contents) {
		String number = "";
		Pattern p = Pattern.compile("\\d+\\.?\\d*");
		Matcher m = p.matcher(contents);
		if (m.find()) {
			number = m.group();
		}
		return number;
	}
}
