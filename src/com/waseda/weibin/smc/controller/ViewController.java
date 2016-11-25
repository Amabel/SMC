package com.waseda.weibin.smc.controller;

import com.waseda.weibin.smc.util.Input;
import com.waseda.weibin.smc.view.CLIView;
import com.waseda.weibin.smc.view.View;

/**
 * @author  Weibin Luo
 * @version Created on Nov 24, 2016 9:12:54 PM
 */
public class ViewController {

	private View view;
	
	public void launch() {
		// TODO Auto-generated method stub
		view = new CLIView();
		
		showMessage("> ");
		String input = getInput();
		boolean b = Input.checkInput(input);
		showMessage(b?"t":"f");
		

	}
	
	private void showMessage(String msg) {
		view.showMessage(msg);
	}
	
	private String getInput() {
		String input = null;
		input = view.getInput();
		return input;
	}
	

	
}
