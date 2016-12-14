package com.waseda.weibin.smc.controller;


import java.awt.Desktop.Action;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * @author  Weibin Luo
 * @version Created on Dec 11, 2016 1:33:28 PM
 */


public class MainFrameController {

    @FXML
    private AnchorPane layoutPane;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private SplitPane leftSplitPane;

    @FXML
    private ListView<String> fileListView;

    @FXML
    private SplitPane rightSplitPane;

    @FXML
    private TextArea fileContent;

    @FXML
    private TextArea messageArea;

    @FXML
    private Button buttonOpen;

    @FXML
    private Button buttonOpen1;

    @FXML
    private Button buttonVerify;
    
    private ObservableList<String> fileNameList = FXCollections.observableArrayList();

    @FXML
    void onContentContextMenuItemSelectAll(ActionEvent event) {

    }
    
    @FXML
    void onMenuItemClose(ActionEvent event) {
    	fileNameList.remove(0);
    }

    @FXML
    void onMenuItemOpen(ActionEvent event) {
    	List<File> files = openFiles();
    	if (files != null) {	
	    	for (File file : files) {
	    		// Update file list
				fileNameList.add(file.getName());
			}
	    	// Set to the list view
	    	setListView();
    	}
    }
    
    @FXML
    void onButtonOpen(ActionEvent event) {
    	onMenuItemOpen(event);
    }
    
    
    
    private List<File> openFiles() {
    	FileChooser fileChooser = new FileChooser();  
        return fileChooser.showOpenMultipleDialog(layoutPane.getScene().getWindow());
    }
    
    private void showFileContents(String fileName) {
    	if (fileName != null) {
    		fileContent.setText(FileProcessor.readFile(fileName));  
    	}
    }
    
    private void setListView() {
    	fileListView.setItems(fileNameList);
    }
    
    @FXML
    void initialize() {
    	// Initialize processes
    	
    	// Create a temporal directory
    	String tempDirName = Constants.TEMP_DIR_NAME;
    	FileProcessor.createDirectioy(tempDirName);
    	
    	// Listeners
    	fileListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					String fileName = fileListView.getSelectionModel().getSelectedItem();
					showFileContents(fileName);
			    }
			}
    	});
    	
    }
}

