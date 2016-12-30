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
//	private String fileNamesInput;
//	private String ltlsInput;
	private List<String> fileNames;
	private String ltl;
	private List<String> variableNames;
	private List<String> fileNamesWithPath;
	private List<String> slicedFileNamesWithPath;
	private int index;
	private ObservableList<Results> resultData;
	
	
	public SMCService(List<String> fileNames, String ltl, int index) {
		this.fileNames = fileNames;
		this.ltl = ltl;
		this.index = index;
	}
	
	public void launch() {
		processFileNames();
		processLTL();
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
		Modex modexOrigin = new Modex(fileNamesWithPath, variableNames, ltl, index);
		modexOrigin.extractModel();
		
		// Extract sliced model
		Modex modexSliced = new Modex(slicedFileNamesWithPath, variableNames, ltl, index);
		modexSliced.extractModel();
	}
	
	public void processModelcheck() {
		System.out.println("\n===== Begin to spin =====");
		String outputDestinationFileName = "_" + index + "_res_nosli.txt";
		spin = new SPIN(fileNamesWithPath, outputDestinationFileName);
		spin.modelCheck();
		System.out.println("output file: " + outputDestinationFileName);
		// Model check the sliced model
		String outputDestinationFileName2 = "_" + index + "_res_sli.txt";
		spin = new SPIN(slicedFileNamesWithPath, outputDestinationFileName2);
		spin.modelCheck();
		
		System.out.println("output file: " + outputDestinationFileName2);
		System.out.println("===== End of spin =====");
	}
	
	private void processSlicedFileNames() {
		slicedFileNamesWithPath = new ArrayList<>();
		for (String fileName : fileNamesWithPath) {
			String name = FileProcessor.getFileNameWithoutSurfix(fileName, ".c") + "_ltl" + index + "_sliced.c";
			slicedFileNamesWithPath.add(name);
		}
	}
	
	private void processFileNames() {
		fileNamesWithPath = new ArrayList<String>();

		for (String fileName : fileNames) {
			String fileNameWithPath = Constants.TEMP_DIR_NAME + fileName;
			fileNamesWithPath.add(fileNameWithPath);
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
			elapsedTime = Output.findOutputNumber("res_nosli.txt", contents);
			elapsedTimeSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			System.out.println("Failed to get elapsed time");
		}
		
		// Find reached depth
		contents = "depth reached " + "\\d+\\.?\\d*";
		String reachedDepth = "";
		String reachedDepthSli = "";
		try {
			reachedDepth = Output.findOutputNumber("res_nosli.txt", contents);
			reachedDepthSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			System.out.println("Failed to get reached depth");
		}
		
		// Find error numbers
		contents = "errors: " + "\\d+\\.?\\d*";
		String errorNumbers = "";
		String errorNumbersSli = "";
		try {
			errorNumbers = Output.findOutputNumber("res_nosli.txt", contents);
			errorNumbersSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			System.out.println("Failed to get error number");
		}
		
		// Find state memory usage
		contents = "\\s*\\d+\\.?\\d*\\s*" + "actual memory usage for states";
		String stateMemoryUsage = "";
		String stateMemoryUsageSli = "";
		try {
			stateMemoryUsage = Output.findOutputNumber("res_nosli.txt", contents);
			stateMemoryUsageSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			System.out.println("Failed to get state memory usage");
		}
		
		// Find total memory usage
		contents = "\\s*\\d+\\.?\\d*\\s*" + "total actual memory usage";
		String totalMemoryUsage = "";
		String totalMemoryUsageSli = "";
		try {
			totalMemoryUsage = Output.findOutputNumber("res_nosli.txt", contents);
			totalMemoryUsageSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			System.out.println("Failed to total memory usage");
		}
		
		resultData = FXCollections.observableArrayList(
					new Results("Elapsed time", elapsedTime, elapsedTimeSli),
					new Results("Depth reached", reachedDepth, reachedDepthSli),
					new Results("Errors found", errorNumbers, errorNumbersSli),
					new Results("mem usage(states)", stateMemoryUsage, stateMemoryUsageSli),
					new Results("mem usage(total)", totalMemoryUsage, totalMemoryUsageSli)
				);
		
	}
	
	public ObservableList<Results> getResultData() {
		return resultData;
	}

}
