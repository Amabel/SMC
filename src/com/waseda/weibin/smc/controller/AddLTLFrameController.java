package com.waseda.weibin.smc.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	private List<String> ltls;
	
	@FXML void onButtonCleartextFieldLTL_0(ActionEvent event) {
		textFieldLTL_0.clear();
		ltls.remove(0);
	}
	
	@FXML void onButtonCleartextFieldLTL_1(ActionEvent event) {
		textFieldLTL_1.clear();
		ltls.remove(1);
	}
	
	@FXML void onButtonCleartextFieldLTL_2(ActionEvent event) {
		textFieldLTL_2.clear();
		ltls.remove(2);
	}
	
	@FXML void onButtonAdd(ActionEvent event) {
		ltls.clear();
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
	
	public AddLTLFrameController(List<String> ltls) {
		this.ltls = ltls;
	}
	
    @FXML void initialize() {
    	System.out.println(ltls);
    	List<TextField> tfs = new ArrayList<>(Arrays.asList(
												    			textFieldLTL_0,
												    			textFieldLTL_1,
												    			textFieldLTL_2
												    		));
    	for (int i=0; i<ltls.size(); i++) {
    		tfs.get(i).setText(ltls.get(i));
    	}
    }
	
}
