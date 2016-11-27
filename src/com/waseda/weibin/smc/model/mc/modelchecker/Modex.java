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

	private List<String> files;
	private List<String> values;
	
	public Modex(List<String> files, List<String> values) {
		// TODO Auto-generated constructor stub
		this.files = files;
		this.values = values;
	}
	
	public void processModelCheck() {
		// TODO Auto-generated method stub
		generatePrxFile();
		extractModel();
	}
	
	private void extractModel() {
		// Execute command "modex xxx.c"
		String fileName = files.get(0);
		String command = "modex " + fileName;
		
		System.out.println("Modex command: " + command);
	}
	
	private void generatePrxFile() {
		String contents = "%X -xe";
		String fileName = FileProcessor.getFileNameWithoutSurfix(files.get(0), ".c") + ".prx";
		FileProcessor.createFile(fileName, contents);
	}

}
