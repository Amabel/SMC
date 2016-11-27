package com.waseda.weibin.smc.model.mc.modelchecker;

import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.util.Command;
import com.waseda.weibin.smc.util.FileProcessor;

/**
 * @author  Weibin Luo
 * @version Created on Nov 23, 2016 12:31:16 PM
 */
public class SPIN extends ModelChecker {

	private String outputDestinationFileName;
	
	public SPIN(String outputDestinationFilename) {
		this.outputDestinationFileName = outputDestinationFilename;
	}
	
	public void modelCheck() {
		doModelCheck();
	}
	
	private void doModelCheck() {
		String command = "spin -run model";
		FileProcessor.deleteFile(outputDestinationFileName);
		Command.executeCommandInShell(command, "spin.sh", outputDestinationFileName);
	}
	
}
