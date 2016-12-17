package com.waseda.weibin.smc.controller;


import java.io.File;
import java.util.List;

import com.waseda.weibin.smc.model.Results;
import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;
import com.waseda.weibin.smc.util.FxDialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Button buttonSave;

    @FXML
    private Button buttonVerify;

    @FXML
    private TextField textFieldLTLFormula;
    
    @FXML
    private Button buttonTest;

    @FXML
    private TableView<Results> tableViewCompare;
    @FXML
    private TableColumn<Results, String> tableViewCompareAttributeColumn;
    @FXML
    private TableColumn<Results, String> tableViewCompareNoSlicingColumn;
    @FXML
    private TableColumn<Results, String> tableViewCompareWithSlicingColumn;
    
    
    private ObservableList<String> fileNameList = FXCollections.observableArrayList();
    private ViewController viewController = new ViewController("gui");

    @FXML
    void onContentContextMenuItemSelectAll(ActionEvent event) {

    }
    
    @FXML
    void onMenuItemClose(ActionEvent event) {
    	fileNameList.remove(0);
    }

    @FXML
    void onMenuItemOpen(ActionEvent event) {
    	openFile();
    }
    
    @FXML
    void onButtonOpen(ActionEvent event) {
    	openFile();
    }
    
    @FXML
    void onMenuItemSave(ActionEvent event) {
    	saveFile();
    }
    
    @FXML
    void onButtonSave(ActionEvent event) {
    	saveFile();
    }
    
    @FXML
    void onTextFieldLTLFormula(ActionEvent event) {
    	verify();
    }
    
    @FXML
    void onButtonVerify(ActionEvent event) {
    	verify();
    }
    
    @FXML
    void onListViewContxtMenuItemRemove(ActionEvent event) {
    	String fileName = fileListView.getSelectionModel().getSelectedItem();
    	removeFileFromListView(fileName);
    }
    @FXML
    void onListViewContxtMenuItemRemoveAll(ActionEvent event) {
    	removeAllFileFromListView();
    }

    
    // Called when menuItemOpen or buttonOpen is clicked
    private void openFile() {
    	List<File> files = openFiles();
    	if (files != null) {	
	    	for (File file : files) {
	    		// Update file list
	    		String fileName = file.getName();
				fileNameList.add(fileName);
				// Copy file to temp directory
	    		String destFilePath = Constants.TEMP_DIR_NAME + fileName;
	    		File destFile = new File(destFilePath);
	    		FileProcessor.copyFile(file, destFile);
			}
	    	// Set to the list view
	    	setListView();
    	}
    }
    // Called by openFile() to open multiple files
    private List<File> openFiles() {
    	FileChooser fileChooser = new FileChooser();  
        return fileChooser.showOpenMultipleDialog(layoutPane.getScene().getWindow());
    }
    // Called by the action listener of listView to show the contents of the file on the right pane
    private void showFileContents(String fileName) {
    	String tempFileName = Constants.TEMP_DIR_NAME + fileName;
    	if (tempFileName != null) {
    		fileContent.setText(FileProcessor.readFile(tempFileName));  
    	}
    }
    
    // Called when menuItemSave or buttonSava is clicked
    private void saveFile() {
    	String content = fileContent.getText();
    	String fileName = fileListView.getSelectionModel().getSelectedItem();
    	System.out.println("Save to file: " + fileName);
    	if (fileName != null) {
    		FileProcessor.writeStringToFile(fileName, content, false);
    	}
    }
    
    private void setListView() {
    	fileListView.setItems(fileNameList);
    }
    
    private void verify() {
    	System.out.println("Verify clicked");
    	String ltlInput = textFieldLTLFormula.getText();
    	String fileName = fileListView.getSelectionModel().getSelectedItem();
    	// When nothing selected
    	if (fileName == null) {
    		fileName = fileNameList.get(0);
    	}
    	System.out.println("Verify file: " + fileName);
    	viewController.setLtlsInput(ltlInput);
    	viewController.setFileNamesInput(fileName);
    	viewController.setStatusProcessInputs();
//    	viewController.launch();
    	
    	
    	viewController.processInputs();
    	viewController.processSlice();
    	viewController.processModelExtract();
    	viewController.processModelcheck();
    	viewController.processOutputs();
    	setResultsToTableView();
    }
    
    // Remove the file on the listview, also remove the file in the temp directory
    private void removeFileFromListView(String fileName) {
    	String filePath = Constants.TEMP_DIR_NAME + fileName;
    	System.out.println("delete file: " + fileName);
    	FileProcessor.deleteFile(filePath);
    	fileNameList.remove(fileName);
    }
    
    private void removeAllFileFromListView() {
//    	System.out.println(fileNameList.toString());
    	for (String fileName : fileNameList) {
			String filePath = Constants.TEMP_DIR_NAME + fileName;
			FileProcessor.deleteFile(filePath);
		}
    	fileNameList.clear();
    }
    
    private void setResultsToTableView() {
    	ObservableList<Results> resultsData = viewController.getResultsData();
    	if (resultsData != null) {
    		tableViewCompare.setItems(resultsData);    		
    	}
    	tableViewCompareAttributeColumn.setCellValueFactory(
    			new PropertyValueFactory<Results, String>("attribute"));
    	tableViewCompareNoSlicingColumn.setCellValueFactory(
				new PropertyValueFactory<Results, String>("noSli"));
    	tableViewCompareWithSlicingColumn.setCellValueFactory(
				new PropertyValueFactory<Results, String>("withSli"));
    	
    }
    
   @FXML
   void onButtonTest(ActionEvent event) {
	   FxDialogs.showInformation(null, "Good!");
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
				// When double clicked
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					String fileName = fileListView.getSelectionModel().getSelectedItem();
					showFileContents(fileName);
			    }
			}
    	});
   
    }
}

