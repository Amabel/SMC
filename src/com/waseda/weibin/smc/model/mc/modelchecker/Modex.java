package com.waseda.weibin.smc.model.mc.modelchecker;

import java.util.List;

import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.util.Command;
import com.waseda.weibin.smc.util.FileProcessor;

/**
 * @author  Weibin Luo
 * @version Created on Nov 25, 2016 4:00:28 PM
 */
public class Modex extends ModelChecker {

	private List<String> fileNames;
	private List<String> variableNames;
	private List<String> ltls;
	private String ltl;
	private int index;
	
	public Modex(List<String> fileNames, List<String> variableNames, List<String> ltls) {
		// TODO Auto-generated constructor stub
		this.fileNames = fileNames;
		this.variableNames = variableNames;
		this.ltls =ltls;
	}
	
	public Modex(List<String> fileNames, List<String> variableNames, String ltl, int index) {
		// TODO Auto-generated constructor stub
		this.fileNames = fileNames;
		this.variableNames = variableNames;
		this.ltl =ltl;
		this.index = index;
	}
	
	public void extractModel() {
		// TODO Auto-generated method stub
		generatePrxFile();
		extractTheModel();
		if (ltls == null) {
			attachLTLToModel();
		} else {
			attachLTLsToModel();			
		}
		changeModelFileName();
	}

	private void extractTheModel() {
		// Execute command "modex xxx.c"
		String fileName = fileNames.get(0);
		String command = "modex " + fileName;
		
		System.out.println("Modex command: " + command);
		Command.executeCommandInShell(command, "modex.sh");
	}
	
	private void generatePrxFile() {
		String contents = "%X -xe";
		String fileName = FileProcessor.getFileNameWithoutSurfix(fileNames.get(0), ".c") + ".prx";
		FileProcessor.createFile(fileName, contents);
	}
	
	private void attachLTLToModel() {
		String contents = "ltl {\n"
						+ ltl
						+ "\n";
		FileProcessor.appendContentsToFile("model", contents);
	}
	
	private void attachLTLsToModel() {
		String contents = "";
		for (String ltl : ltls) {
			contents += "ltl {\n"
					+ ltl
					+ "\n}";
		}
		FileProcessor.appendContentsToFile("model", contents);
	}
	
	private void changeModelFileName() {
		// TODO Auto-generated method stub
		String fileName = fileNames.get(0);
		String destFileName = FileProcessor.getFileNameWithoutSurfix(fileName, ".c") + ".pml";
		FileProcessor.changeFileName("model", destFileName);
	}

}
