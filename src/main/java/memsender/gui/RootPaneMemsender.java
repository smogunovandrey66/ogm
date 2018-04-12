package memsender.gui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;

class RootPaneMemsender extends BorderPane{
    public RootPaneMemsender(){
        super();
        TextArea txtRawItemLog = new TextArea();
        BorderPane pnlItemLog = new BorderPane();
        pnlItemLog.setCenter(txtRawItemLog);

        TextArea txtDecodedItemLog = new TextArea();
        txtDecodedItemLog.setPrefColumnCount(1);
        BorderPane pnlDecodedItemLog = new BorderPane();
        pnlDecodedItemLog.setCenter(txtDecodedItemLog);

        SplitPane splitPaneItemLog = new SplitPane();
        splitPaneItemLog.setOrientation(Orientation.VERTICAL);
        splitPaneItemLog.getItems().add(pnlItemLog);
        splitPaneItemLog.getItems().add(pnlDecodedItemLog);

        ListView<ItemDataMemsender> listLog = new ListView<>();

        SplitPane splitPaneLog = new SplitPane();
        splitPaneLog.setOrientation(Orientation.HORIZONTAL);
        splitPaneLog.getItems().add(listLog);
        splitPaneLog.getItems().add(splitPaneItemLog);
        setCenter(splitPaneLog);

        BorderPane pnlCommand = new BorderPane();
        pnlCommand.setStyle("-fx-background-color:red");

        ComboBox<String> comboBoxCommand = new ComboBox<>();
        comboBoxCommand.getItems().add("0F 0F 0F 0F 0F 0F 0F 0F");
        comboBoxCommand.getItems().add("03");
        comboBoxCommand.setEditable(true);
        AnchorPane pnlComboCommand = new AnchorPane();
        pnlComboCommand.getChildren().add(comboBoxCommand);
        AnchorPane.setTopAnchor(comboBoxCommand, 0.0);
        AnchorPane.setRightAnchor(comboBoxCommand, 0.0);
        AnchorPane.setLeftAnchor(comboBoxCommand, 0.0);
        AnchorPane.setBottomAnchor(comboBoxCommand, 0.0);
        pnlCommand.setCenter(pnlComboCommand);

        Button btnSendCommand = new Button("Отправить");
        pnlCommand.setRight(btnSendCommand);

        BorderPane pnlButtons = new BorderPane();
//        pnlButtons.setStyle("-fx-background-color:blue");
        pnlButtons.setTop(pnlCommand);

//        GridPane gridPaneButtons = new GridPane();
//        gridPaneButtons.setStyle("-fx-background-color:yellow");
//        Label lblIp = new Label("Адрес:");
//        Label lblPort = new Label("Порт:");
//        gridPaneButtons.add(lblIp, 0, 0);
//        gridPaneButtons.add(lblPort, 0, 1);

        AnchorPane anchorPaneButtons;
        try {
            anchorPaneButtons = FXMLLoader.load(getClass().getResource("PanelButtons.fxml"));
        } catch (IOException e) {
            anchorPaneButtons = null;
            e.printStackTrace();
        }

//        HBox hBoxButtons = new HBox();
//        hBoxButtons.setPrefHeight(50);
//        hBoxButtons.setStyle("-fx-background-color:yellow");
//        BorderPane.setMargin(hBoxButtons, new Insets(5.0));
//
//        AnchorPane anchorPaneLabelsIpAndPortTcp = new AnchorPane();
//        anchorPaneLabelsIpAndPortTcp.setStyle("-fx-background-color:red");
//
//        Label lblIp = new Label("Адрес:");
//        Label lblPort = new Label("Порт:");
//        anchorPaneLabelsIpAndPortTcp.getChildren().addAll(lblIp, lblPort);
//        AnchorPane.setRightAnchor(lblIp, 4.0);
//        AnchorPane.setRightAnchor(lblPort, 4.0);
//        AnchorPane.setTopAnchor(lblPort, 20.0);
//
//        hBoxButtons.getChildren().add(anchorPaneLabelsIpAndPortTcp);

//        pnlButtons.setCenter(hBoxButtons);

//        pnlButtons.setCenter(gridPaneButtons);
        if(anchorPaneButtons != null)
            pnlButtons.setBottom(anchorPaneButtons);

        setBottom(pnlButtons);
    }
}
