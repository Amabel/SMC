package com.waseda.weibin.smc.model.slicing.slicer;

import com.waseda.weibin.smc.model.slicing.Slicer;

/**
 * @author  Weibin Luo 
 * @version Created on Nov 23, 2016 12:24:17 PM
 */
public class FramaC extends Slicer {
	String[] files;
	String[] values;
	
	public FramaC(String[] files, String[] values) {
		// TODO Auto-generated constructor stub
		this.files = files;
		this.values = values;
	}
	
	@Override
	public void slice() {
		// TODO Auto-generated method stub
		createCommand();
		
	}
	
	private void createCommand() {
		String str = processFileNames() + "-slice-value " + processValueNames();
		// EG. of command:  
		String cmd = "frama-c " + str + "-then-on 'Slicing export' -print -ocode xxx.c";
		
		System.out.println(cmd);
	}
	
	private String processFileNames() {
		String str = "";
		for (String string : files) {
			str += string + " ";
		}
		return str;
	}
	
	private String processValueNames() {
		String str = "";
		for (String string : values) {
			str += string + " ";
		}
		return str;
	}
	
}
