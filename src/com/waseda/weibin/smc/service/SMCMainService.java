package com.waseda.weibin.smc.service;

import java.util.ArrayList;
import java.util.List;

import com.waseda.weibin.smc.model.CheckProperties;
import com.waseda.weibin.smc.model.Results;

import javafx.collections.ObservableList;

/**
 * @author  Weibin Luo
 * @version Created on Dec 25, 2016 7:57:33 PM
 */
public class SMCMainService {

	private CheckProperties checkProperties;
	private SMCService smcService;
	private int numLTLs = 0;
	private List<ObservableList<Results>> resultDatas = new ArrayList<ObservableList<Results>>();

	
	public SMCMainService(CheckProperties checkProperties) {
		this.checkProperties = checkProperties;
		numLTLs = checkProperties.getLtls().size();
	}
	
	public void start() {
		List<String> fileNames = checkProperties.getFileNames();
		for (int i=0; i<numLTLs; i++) {
			String ltl = checkProperties.getLtls().get(i);
			smcService = new SMCService(fileNames, ltl, i);
			smcService.launch();
			resultDatas.add(smcService.getResultData());
		}

	}

	public List<ObservableList<Results>> getResultDatas() {
		return resultDatas;
	}
	
	

}
