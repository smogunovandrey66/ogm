package memsender.gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Memsender extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        TextArea textArea = new TextArea();
//        textArea.setEditable(false);
        textArea.appendText("sdfsdf\r\n");
        textArea.appendText("sdf55555555555555555555sdf\r\n");
        textArea.setWrapText(true);
        root.setCenter(textArea);
        BorderPane pnlButtons = new BorderPane();
        pnlButtons.setPrefHeight(100);
        pnlButtons.setStyle("-fx-background-color:blue");
        root.setBottom(pnlButtons);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Memsender");
        stage.setWidth(200);
        stage.setHeight(200);
        stage.show();
    }
}
