package com.waseda.weibin.smc.model.mc.modelchecker;

import java.util.ArrayList;
import java.util.List;

import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.util.FileProcessor;

/**
 * @author  Weibin Luo
 * @version Created on Nov 25, 2016 4:00:28 PM
 */
public class Modex extends ModelChecker {

	private List<String> fileNames;
	private List<String> variableNames;
	
	public Modex(List<String> fileNames, List<String> variableNames) {
		// TODO Auto-generated constructor stub
		this.fileNames = fileNames;
		this.variableNames = variableNames;
	}
	
	public void processModelCheck() {
		// TODO Auto-generated method stub
		generatePrxFile();
		extractModel();
	}
	
	private void extractModel() {
		// Execute command "modex xxx.c"
		String fileName = fileNames.get(0);
		String command = "modex " + fileName;
		
		System.out.println("Modex command: " + command);
	}
	
	private void generatePrxFile() {
		String contents = "%X -xe";
		String fileName = FileProcessor.getFileNameWithoutSurfix(fileNames.get(0), ".c") + ".prx";
		FileProcessor.createFile(fileName, contents);
	}

}
