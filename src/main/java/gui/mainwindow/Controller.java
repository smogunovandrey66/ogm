package gui.mainwindow;

import gui.connect.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
    @FXML
    private Button btnConnectForm;

    @FXML
    private void btnConnectFormClick(ActionEvent event) {
//        JFrame frame = new JFrame();
//        frame.setSize(200, 200);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setVisible(true);
        new Stage(((Node)event.getTarget()).getScene().getWindow());
//        try {
//            Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("gui/connect/layout.fxml"));
//            final Stage stage = new Stage();
//            StageStoreDimensions.loadDimensions(stage, Controller.class.getName());
//            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent windowEvent) {
//                    StageStoreDimensions.saveDimensions(stage, Controller.class.getName());
//                }
//            });
//
//            stage.setScene(new Scene(parent));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        BorderPane pane = new BorderPane();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(pane, 300, 300));
//        stage.setResizable(false);
//        stage.setMaxHeight(300);
//        stage.setMaxWidth(300);
//        stage.setMinHeight(300);
//        stage.setMinWidth(300);
//        stage.initModality(Modality.WINDOW_MODAL);
//        stage.initStyle(StageStyle.UTILITY);
//        stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
//        stage.show();
    }
}
