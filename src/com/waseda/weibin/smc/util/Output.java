package com.waseda.weibin.smc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  Weibin Luo
 * @version Created on Nov 28, 2016 9:44:12 AM
 */
public class Output {

	public static String findOutputNumber(String fileName, String contents) throws IOException {
		String res = "";
		String num = "";
		String foundLine = "";
		// Find the line in file
		FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        try {
            File file = new File(fileName);
            
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);

            Pattern p = Pattern.compile(contents);
            for(int i=0; (res =br.readLine())!=null; i++) {
                Matcher m = p.matcher(res);
                if (m.find()) {
                	foundLine = m.group();
                	break;
                }
            }
            
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
		}
		// Find the number in the line
        num = RegularExpression.findNumberInString(foundLine);
		return num;
	}
}
