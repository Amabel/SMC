package com.waseda.weibin.smc.service;

import java.io.File;
import java.util.List;

import com.waseda.weibin.smc.model.Results;
import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;

import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * @author  Weibin Luo
 * @version Created on Dec 19, 2016 3:25:26 PM
 */
public class FileService {
	
	private ObservableList<String> fileNameList;
	
	public FileService(ObservableList<String> fileNameList) {
		this.fileNameList = fileNameList;
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
    	viewController.generateResultData();
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
}
