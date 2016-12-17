package com.waseda.weibin.smc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.waseda.weibin.smc.model.Results;
import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.model.mc.modelchecker.Modex;
import com.waseda.weibin.smc.model.mc.modelchecker.SPIN;
import com.waseda.weibin.smc.model.slicing.Slicer;
import com.waseda.weibin.smc.model.slicing.slicer.FramaC;
import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;
import com.waseda.weibin.smc.util.Input;
import com.waseda.weibin.smc.util.Output;
import com.waseda.weibin.smc.util.ProgramStatus;
import com.waseda.weibin.smc.view.CLIView;
import com.waseda.weibin.smc.view.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author  Weibin Luo
 * @version Created on Nov 24, 2016 9:12:54 PM
 */
public class ViewController {

	private View view;
	private ProgramStatus status;
	private FramaC slicer;
	private Modex modex;
	private SPIN spin;
	private String fileNamesInput;
	private String ltlsInput;
	private List<String> fileNames;
	private List<String> ltls;
	private List<String> variableNames;
	private List<String> slicedFileNames;
	private Boolean guiMode = false;
	private ObservableList<Results> resultsData;
	
	public ViewController() {
		
	}
	public ViewController(String mode) {
		if (mode.equalsIgnoreCase("gui")) {
			this.guiMode = true;
		}
		
	}
	public void setFileNamesInput(String fileNamesInput) {
		this.fileNamesInput = fileNamesInput;
	}

	public void setLtlsInput(String ltlsInput) {
		this.ltlsInput = ltlsInput;
	}
	
	public void setStatusProcessInputs() {
		switchToProcessInputsStatus();
	}
	
	public void launch() {
		// TODO Auto-generated method stub
		view = new CLIView();
		if (!guiMode) {
			fileNamesInput = "";
			switchToInitStatus();
		}
		
		while (true) {
			if (status == ProgramStatus.INITIALIZE) {
				switchToGetInputFileNamesStatus();
			}
			System.out.println("status = " + status);
			switch (status) {
			case GETINPUTFILENAMES:
				// Print the prompt
				showMessage("Input the filename: ");
				getInputFileNames();
				break;
			case GETINPUTLTL:
				// Print the prompt
				showMessage("Input the LTL: ");
				getInputLTL();
				break;
			case PROCESSINPUTS:
				processInputs();
				break;
			case SLICE:
				processSlice();
				break;
			case MODELEXTRACT:
				processModelExtract();
				break;
			case MODELCHECK:
				processModelcheck();
				break;
			case PROCESSOUTPUTS:
				processOutputs();
				break;
			default:
				break;
			}
		}
	}
	

	public void processInputs() {
		// TODO Auto-generated method stub
		processFileNameInputs();
		processLTLInputs();
		switchToSliceStatus();
	}
	
	private void processFileNameInputs() {
		fileNames = new ArrayList<String>();
		// Convert the input to array by the separator " "
		System.out.println("fileNamesInput = " + fileNamesInput);
		String[] inputs = fileNamesInput.split(" ");
		Pattern p = Pattern.compile("(\\w)+\\.c");
		for (String fileName : inputs) {
			if (p.matcher(fileName).matches()) {
				if (!guiMode) {
					fileNames.add(fileName);					
				} else {
					String fileNameWithPath = Constants.TEMP_DIR_NAME + fileName;
					fileNames.add(fileNameWithPath);
				}
			} else {
				System.out.println("Error when processing filename inputs");
			}
		}
	}
	
	private void processLTLInputs() {
		ltls = new ArrayList<String>();
		variableNames = new ArrayList<String>();
		// 
		String[] inputs = ltlsInput.split(" ");
		Pattern pWord = Pattern.compile("\\w+");
		for (String string : inputs) {
			ltls.add(string);
			Matcher m = pWord.matcher(string);
			while (m.find()) {
				String varName = m.group();
				// exclude a number condition
				Pattern pDigit = Pattern.compile("\\d+");
				Matcher m1 = pDigit.matcher(varName);
				if (!m1.matches()) {
					System.out.println("Found variable: " + varName);
					variableNames.add(varName);
				}
			}
		}
	}
	
	private void getInputFileNames() {
		fileNamesInput = view.getInput();
		switchToGetInputLTLStatus();
	}
	
	private void getInputLTL() {
		ltlsInput = view.getInput();
		switchToProcessInputsStatus();
	}
	
	public ObservableList<Results> getResultsData() {
		return resultsData;
	}
	
	public void processSlice() {
		// Do the slice
		System.out.println("\n===== Begin to slice =====");
		slicer = new FramaC(fileNames, variableNames);
		slicer.slice();
		System.out.println("===== End of slicing =====");
		processSlicedFileNames();
		switchToModelExtractStatus();
	}
	
	public void processModelExtract() {
		System.out.println("\n===== Begin to modex =====");
		modex = new Modex(fileNames, variableNames, ltls);
		modex.extractModel();
		
		// Extract sliced model
		Modex modexSliced = new Modex(slicedFileNames, variableNames, ltls);
		modexSliced.extractModel();
		
		System.out.println("===== End of modex =====");
		switchToModelCheckingStatus();
	}
	
	public void processModelcheck() {
		System.out.println("\n===== Begin to spin =====");
		String outputDestinationFileName = "res_nosli.txt";
		spin = new SPIN(fileNames, outputDestinationFileName);
		spin.modelCheck();
		System.out.println("output file: " + outputDestinationFileName);
		// Model check the sliced model
		String outputDestinationFileName2 = "res_sli.txt";
		spin = new SPIN(slicedFileNames, outputDestinationFileName2);
		spin.modelCheck();
		
		System.out.println("output file: " + outputDestinationFileName2);
		System.out.println("===== End of spin =====");
		switchToProcessOutputsStatus();
	}
	
	public void processOutputs() {
		
		// Find elapsed time
		String contents = "elapsed time " + "\\d+\\.?\\d*" + " seconds";
		String elapsedTime = "";
		String elapsedTimeSli = "";
		try {
			elapsedTime = Output.findOutputNumber("res_nosli.txt", contents);
			elapsedTimeSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Find reached depth
		contents = "depth reached " + "\\d+\\.?\\d*";
		String reachedDepth = "";
		String reachedDepthSli = "";
		try {
			reachedDepth = Output.findOutputNumber("res_nosli.txt", contents);
			reachedDepthSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Find error numbers
		contents = "errors: " + "\\d+\\.?\\d*";
		String errorNumbers = "";
		String errorNumbersSli = "";
		try {
			errorNumbers = Output.findOutputNumber("res_nosli.txt", contents);
			errorNumbersSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Find state memory usage
		contents = "\\s*\\d+\\.?\\d*\\s*" + "actual memory usage for states";
		String stateMemoryUsage = "";
		String stateMemoryUsageSli = "";
		try {
			stateMemoryUsage = Output.findOutputNumber("res_nosli.txt", contents);
			stateMemoryUsageSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Find total memory usage
		contents = "\\s*\\d+\\.?\\d*\\s*" + "total actual memory usage";
		String totalMemoryUsage = "";
		String totalMemoryUsageSli = "";
		try {
			totalMemoryUsage = Output.findOutputNumber("res_nosli.txt", contents);
			totalMemoryUsageSli = Output.findOutputNumber("res_sli.txt", contents);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\n===== Begin to output =====\n\n");
		System.out.println("Output:\t\t\tnoSli\tSli");
		// Print elapsed time
		System.out.println("elapsed time:\t\t" + elapsedTime + "\t" + elapsedTimeSli);
		// Print reached depth
		System.out.println("depth reached:\t\t" + reachedDepth + "\t" + reachedDepthSli);
		// Print error numbers
		System.out.println("errors found:\t\t" + errorNumbers + "\t" + errorNumbersSli);
		// Print state memory usage
		System.out.println("memory usage(states):\t" + stateMemoryUsage + "\t" + stateMemoryUsageSli);
		// Print total memory usage
		System.out.println("memory usage(total):\t" + totalMemoryUsage + "\t" + totalMemoryUsageSli);
		System.out.println("\n\n===== End of output =====");
		
		// Output the results to compare.txt
		String strElapsedTime = "elapsed time:\t\t" + elapsedTime + "\t" + elapsedTimeSli;
		String strReachedDepth = "depth reached:\t\t" + reachedDepth + "\t" + reachedDepthSli;
		String strErrorNumbers = "errors found:\t\t" + errorNumbers + "\t" + errorNumbersSli;
		String strStateMemoryUsage = "mem usage(states):\t" + stateMemoryUsage + "\t" + stateMemoryUsageSli;
		String strTotalMemoryUsage = "mem usage(total):\t" + totalMemoryUsage + "\t" + totalMemoryUsageSli;
		String resFileName = "compare.txt";
//		String resFilePath = Constants.TEMP_DIR_NAME + resFileName;
		String strTitle = "\t\t\tno slicing\twith slicing";
		FileProcessor.writeStringToFile(resFileName, strTitle + "\n", true);
		FileProcessor.writeStringToFile(resFileName, strElapsedTime + "\n", true);
		FileProcessor.writeStringToFile(resFileName, strReachedDepth + "\n", true);
		FileProcessor.writeStringToFile(resFileName, strErrorNumbers + "\n", true);
		FileProcessor.writeStringToFile(resFileName, strStateMemoryUsage + "\n", true);
		FileProcessor.writeStringToFile(resFileName, strTotalMemoryUsage + "\n", true);
		resultsData = FXCollections.observableArrayList(
					new Results("Elapsed time", elapsedTime, elapsedTimeSli),
					new Results("Depth reached", reachedDepth, reachedDepthSli),
					new Results("Errors found", errorNumbers, errorNumbersSli),
					new Results("mem usage(states)", stateMemoryUsage, stateMemoryUsageSli),
					new Results("mem usage(total)", totalMemoryUsage, totalMemoryUsageSli)
				);
		
		switchToInitStatus();
	}


	private void showMessage(String msg) {
		view.showMessage(msg);
	}
	
	private void processSlicedFileNames() {
		slicedFileNames = new ArrayList<>();
		for (String fileName : fileNames) {
			String name = FileProcessor.getFileNameWithoutSurfix(fileName, ".c") + "_sliced.c";
			slicedFileNames.add(name);
		}
	}
	
	// Switch status
	private void switchToInitStatus() {
		this.status = ProgramStatus.INITIALIZE;
	}
	private void switchToGetInputFileNamesStatus() {
		this.status = ProgramStatus.GETINPUTFILENAMES;
	}
	private void switchToGetInputLTLStatus() {
		this.status = ProgramStatus.GETINPUTLTL;
	}
	private void switchToProcessInputsStatus() {
		this.status = ProgramStatus.PROCESSINPUTS;
	}
	private void switchToSliceStatus() {
		this.status = ProgramStatus.SLICE;
	}
	private void switchToModelCheckingStatus() {
		this.status = ProgramStatus.MODELCHECK;
	}
	private void switchToModelExtractStatus() {
		this.status = ProgramStatus.MODELEXTRACT;
	}
	private void switchToProcessOutputsStatus() {
		this.status = ProgramStatus.PROCESSOUTPUTS;
	}
}
