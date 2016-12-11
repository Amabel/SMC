package com.waseda.weibin.smc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author  Weibin Luo
 * @version Created on Nov 26, 2016 10:25:57 PM
 */
public class FileProcessor {

	public static Boolean createFile(String fileName) {
		// Delete previous files
		FileProcessor.deleteFile(fileName);
		return createFileWithFileName(fileName);
	}
	
	public static Boolean createFile(String fileName, String contents) {
		Boolean created = false;
		try {
			// Delete previous files
			FileProcessor.deleteFile(fileName);
			createFileWithFileName(fileName);
			writeContentsIntoFile(fileName, contents);
			created = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return created;
	}
	
	public static Boolean appendContentsToFile(String fileName, String contents) {
		if (fileExists(fileName)) {
			try {
				return writeContentsIntoFile(fileName, contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static Boolean fileExists(String fileName) {
		return new File(fileName).exists();
	}
	
	public static Boolean deleteFile(String fileName) {
		Boolean deleted = false;
        File file  = new File(fileName);
        try {
            if(file.exists()){
                file.delete();
                deleted = true;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return deleted;
	}
	
	public static String getFileNameWithoutSurfix(String fileName, String surfix) {
		return fileName.substring(0, fileName.length() - surfix.length());
	}
	
	public static void changeFileName(String fileName, String destFileName) {
		String command = "mv " + fileName + " " + destFileName;
		String shellScriptName = "changeFileName.sh";
		Command.executeCommandInShell(command, shellScriptName);
		FileProcessor.deleteFile(shellScriptName);
	}
	
    public static String readFile(File file) {  
        StringBuilder resultStr = new StringBuilder();  
        try {  
            BufferedReader bReader = new BufferedReader(new FileReader(file));  
            String line = bReader.readLine();  
            while (line != null) {  
                resultStr.append(line); 
                resultStr.append(System.getProperty("line.separator"));
                line = bReader.readLine();  
            }  
            bReader.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return resultStr.toString();  
    }
    
    public static String readFile(String fileName) {
    	File file = new File("fileName");
    	return readFile(file);
    }
  
    public static void writeFile(File file, String str) {  
        try {  
          BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));  
          bWriter.write(str);  
          bWriter.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	private static Boolean writeContentsIntoFile(String fileName, String contents) throws IOException {
		Boolean writed = false;
		// Add a new line
		String newContents = contents+"\n";
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(fileName);
            
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            
            for(int i=0; (temp =br.readLine())!=null; i++) {
                buffer.append(temp);
                // separator
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(newContents);
            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            writed = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
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
		return writed;
	}
	
	private static Boolean createFileWithFileName(String fileName) {
		Boolean created = false;
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
				created = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return created;
	}
	
	
	
	
}
