package com.waseda.weibin.smc.service;

import java.util.List;

import com.waseda.weibin.smc.model.CheckProperties;

/**
 * @author  Weibin Luo
 * @version Created on Dec 25, 2016 7:57:33 PM
 */
public class SMCMainService {

	private CheckProperties checkProperties;
	private SMCService smcService;
	private int index = 0;

	
	public SMCMainService(CheckProperties checkProperties) {
		this.checkProperties = checkProperties;
	}
	
	public void start() {
		List<String> fileNames = checkProperties.getFileNames();
		String ltl = checkProperties.getLtls().get(index);
		smcService = new SMCService(fileNames, ltl, index);
	}
	

}
