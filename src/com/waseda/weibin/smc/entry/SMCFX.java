package com.waseda.weibin.smc.entry;


import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.waseda.weibin.smc.util.Constants;
import com.waseda.weibin.smc.util.FileProcessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author  Weibin Luo
 * @version Created on Dec 11, 2016 1:41:53 PM
 */
public class SMCFX extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/waseda/weibin/smc/view/MainFrame.fxml"));  
        Scene scene = new Scene(root, 800, 600);
        primaryStage.initStyle(StageStyle.DECORATED);  
        primaryStage.setScene(scene);  
        primaryStage.setMinHeight(420);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("SMC");  
        primaryStage.show();
	}
	
    @Override
    public void stop() {
    	// Delete related files (temp directory)
    	File tempFile = new File(Constants.TEMP_DIR_NAME);
    	FileProcessor.deleteDirectory(tempFile);
    }
}
