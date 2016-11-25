package com.waseda.weibin.smc.view;

import com.waseda.weibin.smc.util.Input;

/**
 * @author  Weibin Luo
 * @version Created on Nov 24, 2016 9:11:18 PM
 */
public class CLIView implements View {

	@Override
	public void launch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMessage(String msg) {
		// TODO Auto-generated method stub
		System.out.print(msg);
	}

	@Override
	public String getInput() {
		// TODO Auto-generated method stub
		String input = null;
		input = Input.getInput();
		return input;
	}
	


	
	
}
