package com.waseda.weibin.smc.controller;

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
	
	public void launch() {
		// TODO Auto-generated method stub
		view = new CLIView();
		switchToInitStatus();
		
		while (true) {
			// Print the prompt
			showMessage("> ");
			switchToSliceStatus();
			switch (status) {
			case SLICE:
				processSlice();
				break;
			case MODELCHECK:
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
	
	private String getInput() {
		String input = null;
		input = view.getInput();
		return input;
	}
	
	private void processSlice() {
		// Get the input and check
		String input = getInput();
		boolean inputChecked = Input.checkSliceInput(input);
		if (!inputChecked) {
			showMessage("Usage: file1.c [file 2.c ...] value1 [value2 ...]\n");
			return;
		} else {
			// If passed the check
			// Do the slice
			Slicer slicer = new FramaC(input);
			slicer.slice();
			switchToModelCheckingStatus();
		}
	}
	
	private void processModelcheck() {
		
	}
	
	private void switchToInitStatus() {
		this.status = ProgramStatus.INITIALIZE;
	}
	
	private void switchToSliceStatus() {
		this.status = ProgramStatus.SLICE;
	}
	
	private void switchToModelCheckingStatus() {
		this.status = ProgramStatus.MODELCHECK;
	}
	
}
