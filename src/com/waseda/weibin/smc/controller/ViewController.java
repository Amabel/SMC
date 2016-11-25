package com.waseda.weibin.smc.controller;

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
	}
	
	private void showMessage(String msg) {
		view.showMessage(msg);
	}
}
