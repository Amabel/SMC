package com.waseda.weibin.smc.controller;

import java.util.List;

import com.waseda.weibin.smc.model.mc.ModelChecker;
import com.waseda.weibin.smc.model.mc.modelchecker.Modex;
import com.waseda.weibin.smc.model.slicing.Slicer;
import com.waseda.weibin.smc.model.slicing.slicer.FramaC;
import com.waseda.weibin.smc.util.Input;
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
	private String lastInput;
	private List<String> files;
	private List<String> values;
	
	public void launch() {
		// TODO Auto-generated method stub
		view = new CLIView();
		lastInput = "";
		switchToInitStatus();
		
		while (true) {
			if (status == ProgramStatus.INITIALIZE) {
				status = ProgramStatus.GETINPUT;
			}
			// Print the prompt
			showMessage("> ");
			switch (status) {
			case GETINPUT:
				getInput();
			case SLICE:
				processSlice();
				break;
			case MODELCHECK:
				System.out.println("===== Begin to modex =====");
				processModelcheck();
				break;
			default:
				break;
			}
		}
	}
	
	private void showMessage(String msg) {
		view.showMessage(msg);
	}
	
	private void getInput() {
		lastInput = view.getInput();
		switchToSliceStatus();
	}
	
	private void processSlice() {
		// Check the input
		boolean inputChecked = Input.checkSliceInput(lastInput);
		if (!inputChecked) {
			showMessage("Usage: file1.c [file 2.c ...] value1 [value2 ...]\n");
			return;
		} else {
			// If passed the check
			// Do the slice
			System.out.println("===== Begin to slice =====");
			slicer = new FramaC(lastInput);
			slicer.slice();
			files = slicer.getFiles();
			values = slicer.getValues();
			switchToModelCheckingStatus();
		}
	}
	
	private void processModelcheck() {
		modex = new Modex(files, values);
		modex.processModelCheck();
		switchToInitStatus();
	}
	
	private void switchToInitStatus() {
		this.status = ProgramStatus.INITIALIZE;
	}
	
	private void switchToGetInputStatus() {
		this.status = ProgramStatus.GETINPUT;
	}
	
	private void switchToSliceStatus() {
		this.status = ProgramStatus.SLICE;
	}
	
	private void switchToModelCheckingStatus() {
		this.status = ProgramStatus.MODELCHECK;
	}
	
}
