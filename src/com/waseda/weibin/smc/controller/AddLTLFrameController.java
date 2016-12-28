package com.waseda.weibin.smc.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author  Weibin Luo
 * @version Created on Dec 18, 2016 3:03:13 PM
 */
public class AddLTLFrameController {

	@FXML private TextField textFieldLTL_0;
	@FXML private TextField textFieldLTL_1;
	@FXML private TextField textFieldLTL_2;
	@FXML private Button buttonCancel;
	
	private List<String> ltls = new ArrayList<>();
	
	@FXML void onButtonCleartextFieldLTL_0(ActionEvent event) {
		textFieldLTL_0.clear();
	}
	
	@FXML void onButtonCleartextFieldLTL_1(ActionEvent event) {
		textFieldLTL_1.clear();
	}
	
	@FXML void onButtonCleartextFieldLTL_2(ActionEvent event) {
		textFieldLTL_2.clear();
	}
	
	@FXML void onButtonAdd(ActionEvent event) {
		String ltl = null;
		ltl = textFieldLTL_0.getText();
		if (ltl != null) {
			ltls.add(ltl);
		}
		ltl = textFieldLTL_1.getText();
		if (ltl != null) {
			ltls.add(ltl);
		}
		ltl = textFieldLTL_2.getText();
		if (ltl != null) {
			ltls.add(ltl);
		}
		
		System.out.println("ltls: " + ltls.toString());
	}
	
	@FXML void onButtonCancel(ActionEvent event) {
		Stage stage = (Stage) buttonCancel.getScene().getWindow();
		stage.close();
	}
	
	public AddLTLFrameController(int a) {
		System.out.println("1231231231");
	}

	public void aaa() {
		// TODO Auto-generated method stub
		System.out.println("aaaaaaaa");
	}
	
}
