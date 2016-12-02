package com.waseda.weibin.smc.model.mc.modelchecker;

import java.util.List;

import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.util.Command;
import com.waseda.weibin.smc.util.FileProcessor;

/**
 * @author  Weibin Luo
 * @version Created on Nov 23, 2016 12:31:16 PM
 */
public class SPIN extends ModelChecker {

	private String outputDestinationFileName;
	private List<String> fileNames;
	private int mStep = 200000;
	
	public SPIN(List<String> fileNames, String outputDestinationFilename) {
		this.fileNames = fileNames;
		this.outputDestinationFileName = outputDestinationFilename;
	}
	
	public SPIN(List<String> fileNames, String outputDestinationFilename, int mStep) {
		this.fileNames = fileNames;
		this.outputDestinationFileName = outputDestinationFilename;
		this.mStep = mStep;
	}
	
	public void modelCheck() {
		doModelCheck();
	}
	
	private void doModelCheck() {
		String fileName = FileProcessor.getFileNameWithoutSurfix(fileNames.get(0), ".c") + ".pml";
		String command = "spin -run -m" + mStep + " " + fileName;
		System.out.println("SPIN command: " + command);
		FileProcessor.deleteFile(outputDestinationFileName);
		Command.executeCommandInShell(command, "spin.sh", outputDestinationFileName);
	}
	
}
