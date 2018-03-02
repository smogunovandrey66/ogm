package gui;

import core.service.events.ServiceSettings;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.*;

import java.util.prefs.Preferences;

public class StageStoreDimensions extends Stage {
    private Rectangle2D rect = Screen.getPrimary().getVisualBounds();

    protected final static Preferences pref = ServiceSettings.pref();
    protected boolean needCheckFirstLoad = false;

    public static final String WIDTH = "WIDTH";
    public static final String HEIGHT = "HEIGHT";
    public static final String X = "X";
    public static final String Y = "Y";
    public static final String FULLSCREEN = "MAXIMAZED";
    public static final String FIRSTLOAD = "FIRSTLOAD";

    private void setEventHandlers() {
        this.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                loadDimensions(StageStoreDimensions.this, StageStoreDimensions.this.getClass().getName());
            }
        });
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                saveDimensions(StageStoreDimensions.this, StageStoreDimensions.this.getClass().getName());
            }
        });
    }

    public StageStoreDimensions() {
        super();
        setEventHandlers();
    }

    public StageStoreDimensions(StageStyle stageStyle) {
        super(stageStyle);
        setEventHandlers();
    }

    public static void loadDimensions(Stage stage, String id) {
        Preferences pref = StageStoreDimensions.pref.node(id);
        Rectangle2D rect = Screen.getPrimary().getVisualBounds();
        stage.setWidth(pref.getDouble(WIDTH, rect.getWidth()));
        stage.setHeight(pref.getDouble(HEIGHT, rect.getWidth()));
        stage.setX(pref.getDouble(X, 0));
        stage.setY(pref.getDouble(Y, 0));
    }

    public static void loadDimensions(Stage stage){
        loadDimensions(stage, stage.getClass().getName());
    }

    public static void saveDimensions(Stage stage, String id) {
        Preferences pref = StageStoreDimensions.pref.node(id);
        pref.putDouble(WIDTH, stage.getWidth());
        pref.putDouble(HEIGHT, stage.getHeight());
        pref.putDouble(X, stage.getX());
        pref.putDouble(Y, stage.getY());
    }

    public static void saveDimensions(Stage stage){
        saveDimensions(stage, stage.getClass().getName());
    }

    public static void setCenter(Stage stage, Window owner) {
        Rectangle2D rect = Screen.getPrimary().getVisualBounds();
        Pane parent = (Pane) stage.getScene().getRoot();

        if (owner != null)
            for (Screen screen : Screen.getScreens()) {
                if (screen.getVisualBounds().contains(owner.getX(), owner.getY()))
                    rect = screen.getVisualBounds();

            }

        double x = rect.getMinX() + rect.getWidth() / 2;
        double y = rect.getMinY() + rect.getHeight() / 2;

        if(owner != null){
            x -= parent.getPrefWidth() / 2;
            y -= parent.getPrefHeight() / 2;
        }

        stage.setX(x);
        stage.setY(y);
    }

    public static void setCenter(Stage stage){
        setCenter(stage, null);
    }
}
