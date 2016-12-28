package com.waseda.weibin.smc.model;

import java.util.List;

/**
 * @author  Weibin Luo
 * @version Created on Dec 22, 2016 1:11:33 PM
 */
public class CheckProperties {

	private List<String> fileNames;
	private List<String> ltls;
	
	public CheckProperties() {
		
	}
	
	public CheckProperties(List<String> fileNames, List<String> ltls) {
		this.fileNames = fileNames;
		this.ltls = ltls;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public List<String> getLtls() {
		return ltls;
	}

	public void setLtls(List<String> ltls) {
		this.ltls = ltls;
	}
	
	
}

