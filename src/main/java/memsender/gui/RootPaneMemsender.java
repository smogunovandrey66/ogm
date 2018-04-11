package memsender.gui;

import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
        comboBoxCommand.getItems().add("0F");
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
        pnlButtons.setStyle("-fx-background-color:blue");
        pnlButtons.setBottom(pnlCommand);
        setBottom(pnlButtons);
    }
}
