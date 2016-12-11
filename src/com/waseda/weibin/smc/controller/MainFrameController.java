package com.waseda.weibin.smc.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

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
    private ListView<?> fileListView;

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

    @FXML
    void onContentContextMenuItemSelectAll(ActionEvent event) {

    }

    @FXML
    void onMenuItemClose(ActionEvent event) {

    }

    @FXML
    void onMenuItemOpen(ActionEvent event) {

    }

}

