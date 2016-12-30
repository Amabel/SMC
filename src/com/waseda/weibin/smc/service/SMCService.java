package com.waseda.weibin.smc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.waseda.weibin.smc.model.Results;
import com.waseda.weibin.smc.model.mc.modelchecker.Modex;
import com.waseda.weibin.smc.model.mc.modelchecker.SPIN;
import com.waseda.weibin.smc.model.slicing.slicer.FramaC;
import com.waseda.weibin.smc.util.Command;
import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;
import com.waseda.weibin.smc.util.Output;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author  Weibin Luo
 * @version Created on Dec 27, 2016 8:09:23 PM
 */
public class SMCService {
	
//	private FramaC slicer;
//	private Modex modex;
	private SPIN spin;
	private List<String> fileNames;
	private String ltl;
	private List<String> variableNames;
	private List<String> fileNamesWithPath;
	private List<String> fileNamesWithPathAndIndex;
	private List<String> slicedFileNamesWithPathAndIndex;
	private int index;
	private ObservableList<Results> resultData;
	private String resultNoSliFileName;
	private String resultWithSliFileName;
	
	
	public SMCService(List<String> fileNames, String ltl, int index) {
		this.fileNames = fileNames;
		this.ltl = ltl;
		this.index = index;
	}
	
	public void launch() {
		processFileNames();
		processLTL();
		copyFiles();
		processSlice();
		processModelExtract();
		processModelcheck();
		generateResultData();
	}
	
	public void processSlice() {
		// Do slicing
		FramaC slicer = new FramaC(fileNamesWithPath, variableNames);
		slicer.slice();
		processSlicedFileNames();
	}
	
	public void processModelExtract() {
		// Extract origin model
		Modex modexOrigin = new Modex(fileNamesWithPathAndIndex, variableNames, ltl, index);
		modexOrigin.extractModel();
		
		// Extract sliced model
		Modex modexSliced = new Modex(slicedFileNamesWithPathAndIndex, variableNames, ltl, index);
		modexSliced.extractModel();
	}
	
	public void processModelcheck() {
		System.out.println("\n===== Begin to spin =====");
		resultNoSliFileName = Constants.TEMP_DIR_NAME + "res_nosli_" + "ltl" + index + ".txt";
		spin = new SPIN(fileNamesWithPathAndIndex, resultNoSliFileName);
		spin.modelCheck();
		System.out.println("output file: " + resultNoSliFileName);
		// Model check the sliced model
		resultWithSliFileName = Constants.TEMP_DIR_NAME + "res_sli_" + "ltl" + index + ".txt";
		spin = new SPIN(slicedFileNamesWithPathAndIndex, resultWithSliFileName);
		spin.modelCheck();
		
		System.out.println("output file: " + resultWithSliFileName);
		System.out.println("===== End of spin =====");
	}
	
	private void processSlicedFileNames() {
		slicedFileNamesWithPathAndIndex = new ArrayList<>();
		for (String fileName : fileNamesWithPath) {
			String name = FileProcessor.getFileNameWithoutSurfix(fileName, ".c") + "_sliced_ltl" + index + ".c";
			slicedFileNamesWithPathAndIndex.add(name);
		}
	}
	
	private void processFileNames() {
		// Add dir to fileNames
		fileNamesWithPath = new ArrayList<String>();
		for (String fileName : fileNames) {
			String fileNameWithPath = Constants.TEMP_DIR_NAME + fileName;
			fileNamesWithPath.add(fileNameWithPath);
		}
		// Add index to fileNameWithPath
		fileNamesWithPathAndIndex = new ArrayList<String>();
		for (String fileNameswithPath : fileNamesWithPath) {
			String fileNameWithPathAndIndex = FileProcessor.getFileNameWithoutSurfix(fileNameswithPath, ".c") + "_ltl" + index + ".c";
			fileNamesWithPathAndIndex.add(fileNameWithPathAndIndex);
		}
	}
	private void processLTL() {
		Pattern pWord = Pattern.compile("\\w+");
		variableNames = new ArrayList<String>();
		Matcher m = pWord.matcher(ltl);
		while (m.find()) {
			String varName = m.group();
			// exclude a number condition
			Pattern pDigit = Pattern.compile("\\d+");
			Matcher m1 = pDigit.matcher(varName);
			if (!m1.matches() && !variableNames.contains(varName)) {
				System.out.println("Found variable: " + varName);
				variableNames.add(varName);
			}
		}
	}
	
	public void generateResultData() {
		
		// Find elapsed time
		String contents = "elapsed time " + "\\d+\\.?\\d*" + " seconds";
		String elapsedTime = "";
		String elapsedTimeSli = "";
		try {
			elapsedTime = Output.findOutputNumber(resultNoSliFileName, contents);
			elapsedTimeSli = Output.findOutputNumber(resultWithSliFileName, contents);
		} catch (IOException e) {
			System.out.println("Failed to get elapsed time");
		}
		
		// Find reached depth
		contents = "depth reached " + "\\d+\\.?\\d*";
		String reachedDepth = "";
		String reachedDepthSli = "";
		try {
			reachedDepth = Output.findOutputNumber(resultNoSliFileName, contents);
			reachedDepthSli = Output.findOutputNumber(resultWithSliFileName, contents);
		} catch (IOException e) {
			System.out.println("Failed to get reached depth");
		}
		
		// Find error numbers
		contents = "errors: " + "\\d+\\.?\\d*";
		String errorNumbers = "";
		String errorNumbersSli = "";
		try {
			errorNumbers = Output.findOutputNumber(resultNoSliFileName, contents);
			errorNumbersSli = Output.findOutputNumber(resultWithSliFileName, contents);
		} catch (IOException e) {
			System.out.println("Failed to get error number");
		}
		
		// Find state memory usage
		contents = "\\s*\\d+\\.?\\d*\\s*" + "actual memory usage for states";
		String stateMemoryUsage = "";
		String stateMemoryUsageSli = "";
		try {
			stateMemoryUsage = Output.findOutputNumber(resultNoSliFileName, contents);
			stateMemoryUsageSli = Output.findOutputNumber(resultWithSliFileName, contents);
		} catch (IOException e) {
			System.out.println("Failed to get state memory usage");
		}
		
		// Find total memory usage
		contents = "\\s*\\d+\\.?\\d*\\s*" + "total actual memory usage";
		String totalMemoryUsage = "";
		String totalMemoryUsageSli = "";
		try {
			totalMemoryUsage = Output.findOutputNumber(resultNoSliFileName, contents);
			totalMemoryUsageSli = Output.findOutputNumber(resultWithSliFileName, contents);
		} catch (IOException e) {
			System.out.println("Failed to get total memory usage");
		}
		
		resultData = FXCollections.observableArrayList(
					new Results("Elapsed time", elapsedTime, elapsedTimeSli),
					new Results("Depth reached", reachedDepth, reachedDepthSli),
					new Results("Errors found", errorNumbers, errorNumbersSli),
					new Results("mem usage(states)", stateMemoryUsage, stateMemoryUsageSli),
					new Results("mem usage(total)", totalMemoryUsage, totalMemoryUsageSli)
				);
		
	}
	
	private void copyFiles() {
		for (String fileName : fileNamesWithPath) {
			String command = "cp " + fileName + " " + FileProcessor.getFileNameWithoutSurfix(fileName, ".c") + "_ltl" + index + ".c";
			String shellScriptName = "copy.sh";
			Command.executeCommandInShell(command, shellScriptName);
		}
	}
	
	public ObservableList<Results> getResultData() {
		return resultData;
	}

}
