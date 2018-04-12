package memsender.gui;

import core.ServiceSettings;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Memsender extends Application{
    public static void main(String[] args) {
        ServiceSettings.init();
        String s = ServiceSettings.pref("uu").get("aaa", "ooo");
        ServiceSettings.pref("uu").put("aaa", "zzz");
        launch(Memsender.class, args);
        System.out.println("run");
    }

    @Override
    public void start(final Stage stage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("LayoutMemsender.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Memsender");

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ServiceSettings.saveDimensions(stage, RootPaneMemsender.class.getName());
            }
        });

        if(ServiceSettings.pref(RootPaneMemsender.class).getBoolean(ServiceSettings.EXIST, false)){
            ServiceSettings.loadDimensions(stage, RootPaneMemsender.class.getName(), 0, 0, 400, 300);
        } else{
            ServiceSettings.setCenter(stage, 400, 300, null);
        }

        stage.show();
    }
}
