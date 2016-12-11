package com.waseda.weibin.smc.entry;


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
        Scene scene = new Scene(root, 600, 400);  
        primaryStage.initStyle(StageStyle.DECORATED);  
        primaryStage.setScene(scene);  
        primaryStage.setTitle("JavaFX记事本");  
        primaryStage.show();
	}
}
