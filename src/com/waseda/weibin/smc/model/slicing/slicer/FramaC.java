package com.waseda.weibin.smc.model.slicing.slicer;

import java.util.List;
import com.waseda.weibin.smc.model.slicing.Slicer;
import com.waseda.weibin.smc.util.Command;
import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;

/**
 * @author  Weibin Luo 
 * @version Created on Nov 23, 2016 12:24:17 PM
 */
public class FramaC extends Slicer {
	
	private String input;
	private List<String> fileNames;
	private List<String> variableNames;
	private String targetDirectory = "examples";
	private int index;
	
	public FramaC(List<String> fileNames, List<String> variableNames) {
		// TODO Auto-generated constructor stub
		this.fileNames = fileNames;
		this.variableNames = variableNames;
	}
	
	public FramaC(List<String> fileNames, List<String> variableNames, int index) {
		// TODO Auto-generated constructor stub
		this.fileNames = fileNames;
		this.variableNames = variableNames;
		this.index = index;
	}
	
	@Override
	public void slice() {
		// TODO Auto-generated method stub
//		String dir = Constants.TEMP_DIR_NAME;
//		Command.switchDir(dir);
		String command = createCommand();
		doSlice(command);
	}
	
	// Getters
	public List<String> getFiles() {
		return fileNames;
	}
	public List<String> getValues() {
		return variableNames;
	}
	
	private void doSlice(String command) {
		String shellScriptName = "slice.sh";
		Command.executeCommandInShell(command, shellScriptName);
		// Change the sliced filename to "*_sliced.c"
//		changeSlicedFileName(files.get(0));
	}
	
	private void changeSlicedFileName(String fileName) {
		// TODO Auto-generated method stub
		String fileNameWithoutSurfix = FileProcessor.getFileNameWithoutSurfix(fileName, ".c");
		String command = "mv " + fileNameWithoutSurfix + "_sliced.c? " + fileNameWithoutSurfix + "_sliced.c ";
		String shellScriptName = "changeSlicedFile.sh";
		Command.executeCommandInShell(command, shellScriptName);
	}

	private void changeCurrentDirectory() {
		// Switch to targetDirectory 
		String cmd = "cd" + targetDirectory;
		Command.execute(cmd);
	}
	
	private String createCommand() {
		String cmd = "";
		String str = processFileNames() + " -slice-value " + processValueNames();
		// Generate the target filename
		String targetFileName = FileProcessor.getFileNameWithoutSurfix(fileNames.get(0), ".c") + "_sliced_ltl" + index + ".c";
		// Generate the slicing command
		// Eg. of command: $ frama-c <source files> <desired slicing mode and appropriate options> -then-on 'Slicing export' -print
		// Create a new file
		cmd = "frama-c " + str + " -then-on 'Slicing export' -print -ocode " + targetFileName;
		// Does not create a new file
//		cmd = "frama-c " + str + "-then-on 'Slicing export' -print";
		
//		Log4j2.logger.trace("===== Print the slicing command =====");
//		Log4j2.logger.trace(cmd);
		
		System.out.println(cmd);
		
		return cmd;
	}
	
	// Convert Arraylist files into a single string 
	private String processFileNames() {
		String str = String.join(" ", fileNames);
		return str;
	}
	// Convert Arraylist values into a single string 
	private String processValueNames() {
		String str = String.join(",", variableNames);
		return str;
	}
	
}
