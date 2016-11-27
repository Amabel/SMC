package com.waseda.weibin.smc.model.slicing;

import java.util.List;

/**
 * @author  Weibin Luo
 * @version Created on Nov 23, 2016 12:32:19 PM
 */
public abstract class Slicer {
	
	private List<String> files;
	public List<String> getFiles() {
		return files;
	}

	public List<String> getValues() {
		return values;
	}

	private List<String> values;
	
	public abstract void slice();

	public List<String> getFiles1() {
		// TODO Auto-generated method stub
		return null;
	}
}
