package com.waseda.weibin.smc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.waseda.weibin.smc.model.CheckProperties;
import com.waseda.weibin.smc.model.Results;
import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;

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
			outputToLog();
		}

	}

	public List<ObservableList<Results>> getResultDatas() {
		System.out.println("resultDatas:" + resultDatas);
		return resultDatas;
	}
	
	private void outputToLog() {
		// Get current time
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = dateFormat.format(date);
		
		// Output file name
		String outputFileName = time + ".log";
		String outputFileNameWithDir = Constants.LOG_DIR_NAME + outputFileName;
		
		// Create file
		FileProcessor.createFile(outputFileNameWithDir);
		
		for (int i=0; i<numLTLs; i++) {
			// Each ltl
			String contents = "ltl_" + i + ": " + checkProperties.getLtls().get(i);
			FileProcessor.appendContentsToFile(outputFileNameWithDir, contents);
			
			// Each line
			ObservableList<Results> resultData = resultDatas.get(i);
			for (Results results : resultData) {
				// attribute	nosli	withsli
				String attribute = results.getAttribute();
				String noSli = results.getNoSli();
				String withSli = results.getWithSli();
				contents = attribute + "\t" + noSli + "\t" + withSli;
				FileProcessor.appendContentsToFile(outputFileNameWithDir, contents);
			}
		}
	}
	

}
