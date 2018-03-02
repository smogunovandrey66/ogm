package gui.mainwindow;

import gui.StageStoreDimensions;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import localserver.ServiceLocalServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWindow extends Application{
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
    private Stage stage;

    private static void initServices(){
        ServiceLocalServer.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        logger.debug(getClass().getResource("layout.fxml").toExternalForm());
        StageStoreDimensions.loadDimensions(stage, MainWindow.class.getName());
        this.stage = stage;
        stage.setTitle("Конфигуратор ОГМ-140");
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(getClass().getResource("layout.css").toExternalForm());
        stage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                logger.debug("Создана главная форма приложения");
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                StageStoreDimensions.saveDimensions(MainWindow.this.stage, MainWindow.class.getName());
                logger.debug("Закрыта главная форма");
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        initServices();
        launch(args);
    }
}
