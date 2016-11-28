package com.waseda.weibin.smc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.model.mc.modelchecker.Modex;
import com.waseda.weibin.smc.model.mc.modelchecker.SPIN;
import com.waseda.weibin.smc.model.slicing.Slicer;
import com.waseda.weibin.smc.model.slicing.slicer.FramaC;
import com.waseda.weibin.smc.util.FileProcessor;
import com.waseda.weibin.smc.util.Input;
import com.waseda.weibin.smc.util.Output;
import com.waseda.weibin.smc.util.ProgramStatus;
import com.waseda.weibin.smc.view.CLIView;
import com.waseda.weibin.smc.view.View;

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
	
	public void launch() {
		// TODO Auto-generated method stub
		view = new CLIView();
		fileNamesInput = "";
		switchToInitStatus();
		
		while (true) {
			if (status == ProgramStatus.INITIALIZE) {
				switchToGetInputFileNamesStatus();
			}
			switch (status) {
			case GETINPUTFILENAMES:
				// Print the prompt
				showMessage("> ");
				getInputFileNames();
				break;
			case GETINPUTLTL:
				// Print the prompt
				showMessage("> ");
				getInputLTL();
				break;
			case PROCESSINPUTS:
				processInputs();
				break;
			case SLICE:
				processSlice();
				processSlicedFileNames();
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
	

	private void processInputs() {
		// TODO Auto-generated method stub
		processFileNameInputs();
		processLTLInputs();
		switchToSliceStatus();
	}
	
	private void processFileNameInputs() {
		fileNames = new ArrayList<String>();
		// Convert the input to array by the separator " "
		String[] inputs = fileNamesInput.split(" ");
		Pattern p = Pattern.compile("(\\w)+\\.c");
		for (String string : inputs) {
			if (p.matcher(string).matches()) {
				fileNames.add(string);
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
	
	private void processSlice() {
		// Do the slice
		System.out.println("\n===== Begin to slice =====");
		slicer = new FramaC(fileNames, variableNames);
		slicer.slice();
		System.out.println("===== End of slicing =====");
		switchToModelExtractStatus();
	}
	
	private void processModelExtract() {
		System.out.println("\n===== Begin to modex =====");
		modex = new Modex(fileNames, variableNames, ltls);
		modex.extractModel();
		
		// Extract sliced model
		Modex modexSliced = new Modex(slicedFileNames, variableNames, ltls);
		modexSliced.extractModel();
		
		System.out.println("===== End of modex =====");
		switchToModelCheckingStatus();
	}
	
	private void processModelcheck() {
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
	
	private void processOutputs() {
		System.out.println("\n===== Begin to output =====");
		
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
		// Print elapsed time
		System.out.println("elapsed time:\t\t" + elapsedTime + " seconds");
		System.out.println("elapsed time (sli):\t: " + elapsedTimeSli + " seconds");
		
		
		System.out.println("\n===== End of output =====");
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
