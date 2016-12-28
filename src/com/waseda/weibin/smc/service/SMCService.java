package com.waseda.weibin.smc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.waseda.weibin.smc.model.mc.modelchecker.Modex;
import com.waseda.weibin.smc.model.mc.modelchecker.SPIN;
import com.waseda.weibin.smc.model.slicing.slicer.FramaC;
import com.waseda.weibin.smc.util.FileProcessor;

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
	private List<String> slicedFileNames;
	private int index;
	
	
	public SMCService(List<String> fileNames, String ltl, int index) {
		this.fileNames = fileNames;
		this.ltl = ltl;
		this.index = index;
	}
	
	public void launch() {
		processLTL();
		processSlice();
		processModelExtract();
		processModelcheck();
	}
	
	public void processSlice() {
		// Do slicing
		FramaC slicer = new FramaC(fileNames, variableNames);
		slicer.slice();
		processSlicedFileNames();
	}
	
	public void processModelExtract() {
		// Extract origin model
		Modex modexOrigin = new Modex(fileNames, variableNames, ltl, index);
		modexOrigin.extractModel();
		
		// Extract sliced model
		Modex modexSliced = new Modex(slicedFileNames, variableNames, ltl, index);
		modexSliced.extractModel();
	}
	
	public void processModelcheck() {
		System.out.println("\n===== Begin to spin =====");
		String outputDestinationFileName = "_" + index + "_res_nosli.txt";
		spin = new SPIN(fileNames, outputDestinationFileName);
		spin.modelCheck();
		System.out.println("output file: " + outputDestinationFileName);
		// Model check the sliced model
		String outputDestinationFileName2 = "_" + index + "_res_sli.txt";
		spin = new SPIN(slicedFileNames, outputDestinationFileName2);
		spin.modelCheck();
		
		System.out.println("output file: " + outputDestinationFileName2);
		System.out.println("===== End of spin =====");
	}
	
	private void processSlicedFileNames() {
		slicedFileNames = new ArrayList<>();
		for (String fileName : fileNames) {
			String name = FileProcessor.getFileNameWithoutSurfix(fileName, ".c") + "_ltl" + index + "_sliced.c";
			slicedFileNames.add(name);
		}
	}
	
	private void processLTL() {
		Pattern pWord = Pattern.compile("\\w+");
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

}
