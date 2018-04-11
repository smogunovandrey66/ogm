package memsender.gui;

import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.PaneBuilder;

class RootPaneMemsender extends BorderPane{
    public RootPaneMemsender(){
        super();
        TextArea txtRawItemLog = new TextArea();
        BorderPane pnlItemLog = new BorderPane();
        pnlItemLog.setCenter(txtRawItemLog);

        TextArea txtDecodedItemLog = new TextArea();
        BorderPane pnlDecodedItemLog = new BorderPane();
        pnlDecodedItemLog.setCenter(txtDecodedItemLog);

        SplitPane splitPaneItemLog = new SplitPane();
        splitPaneItemLog.setOrientation(Orientation.VERTICAL);
        splitPaneItemLog.getItems().add(pnlItemLog);
        splitPaneItemLog.getItems().add(pnlDecodedItemLog);

        ListView<ItemDataMemsender> listLog = new ListView<>();

        SplitPane splitPaneListItem = new SplitPane();
        splitPaneListItem.setOrientation(Orientation.HORIZONTAL);
        splitPaneListItem.getItems().addAll(listLog, splitPaneItemLog);

        BorderPane pnlLogAndCommand = new BorderPane();
        pnlLogAndCommand.setCenter(splitPaneListItem);

        Pane pnlCommand = new Pane();
        pnlCommand.setPrefHeight(50);

        pnlLogAndCommand.setBottom(pnlCommand);

        setCenter(pnlLogAndCommand);

        BorderPane pnlButtons = new BorderPane();
        pnlButtons.setPrefHeight(100);
        pnlButtons.setStyle("-fx-background-color:blue");
        setBottom(pnlButtons);
    }
}
