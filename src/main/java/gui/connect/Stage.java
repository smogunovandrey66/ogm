package gui.connect;

import core.ServiceSettings;
import gui.StageStoreDimensions;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Stage extends javafx.stage.Stage {
    private final String EXIST = "EXIST";
    private boolean exist = false;
    private Window owner;

    private void makeUI() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("layout.fxml"));

            setScene(new Scene(parent));

            getScene().getStylesheets().clear();
            getScene().getStylesheets().add(getClass().getResource("layout.css").toExternalForm());


            initModality(Modality.WINDOW_MODAL);
            initStyle(StageStyle.UTILITY);
            initOwner(owner);

            exist = ServiceSettings.node(Stage.class.getName()).getBoolean(EXIST, false);

            setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Preferences p = ServiceSettings.node(Stage.this);
                    p.putBoolean(EXIST, true);
                    StageStoreDimensions.saveDimensions(Stage.this, Stage.class.getName());
                }
            });

            //Если существуют настройки, то их используем для установки размером и положения
            if (exist)
                StageStoreDimensions.loadDimensions(this);
                //Иначе отталкиваемся от формы, которая вызвала текущую форму и по размещаем по центру,
                //используя размеры из макета
            else
                StageStoreDimensions.setCenter(this, owner);

            show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage(Window owner) {
        super();
        this.owner = owner;
        makeUI();
    }
}
