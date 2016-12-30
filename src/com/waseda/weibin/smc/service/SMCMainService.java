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
	private int index = 0;
	private List<ObservableList<Results>> resultDatas = new ArrayList<ObservableList<Results>>();

	
	public SMCMainService(CheckProperties checkProperties) {
		this.checkProperties = checkProperties;
	}
	
	public void start() {
		List<String> fileNames = checkProperties.getFileNames();
		String ltl = checkProperties.getLtls().get(index);
		smcService = new SMCService(fileNames, ltl, index);
		smcService.launch();
		resultDatas.add(smcService.getResultData());
	}

	public List<ObservableList<Results>> getResultDatas() {
		return resultDatas;
	}
	
	

}
