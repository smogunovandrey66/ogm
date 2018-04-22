package core;


import core.platformconst.PlatformConst;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Сервис настроек приложения <font style = "color: red"><b>ОГМ</b></font>
 */
public class ServiceSettings {

    public static final Path DEFAULT_PATH_OGM = Paths.get(System.getProperty("user.home")).resolve("ogm");
    private static ServiceSettings service = null;
    private Preferences preferences;

    private ServiceSettings() {
        preferences = Preferences.userRoot().node(PlatformConst.makeVersion());
    }

    public final static String WIDTH = "WIDTH";
    public final static String HEIGHT = "HEIGHT";
    public final static String Y = "Y";
    public final static String X = "X";
    public final static String EXIST = "EXIST";

    public static Preferences pref() {
        init();
        return service.preferences;
    }

    public static void init() {
        if (service == null)
            service = new ServiceSettings();
    }

    public static Preferences pref(String id) {
        init();
        return service.preferences.node(id);
    }

    public static Preferences pref(Class<?> clazz) {
        return pref(clazz.getName());
    }

    public static Preferences pref(Object object) {
        return pref(object.getClass());
    }

    public static Preferences node(String id) {
        return service.preferences.node(id);
    }

    public static Preferences node(Class<?> clazz) {
        return service.preferences.node(clazz.getName());
    }

    public static Preferences node(Object object) {
        return node(object.getClass());
    }

    public static void loadDimensions(Stage stage, String id, double x, double y, double width, double height) {
        Preferences pref = ServiceSettings.pref(id);
        stage.setX(pref.getDouble(X, x));
        stage.setY(pref.getDouble(Y, y));
        stage.setWidth(pref.getDouble(WIDTH, width));
        stage.setHeight(pref.getDouble(HEIGHT, height));
    }

    public static void saveDimensions(Stage stage, String id) {
        Preferences pref = ServiceSettings.pref(id);
        pref.putBoolean(EXIST, true);
        pref.putDouble(X, stage.getX());
        pref.putDouble(Y, stage.getY());
        pref.putDouble(WIDTH, stage.getWidth());
        pref.putDouble(HEIGHT, stage.getHeight());
    }

    public static void setCenter(Stage stage, double width, double height, Window owner) {
        stage.setWidth(width);
        stage.setHeight(height);

        Rectangle2D rect = Screen.getPrimary().getVisualBounds();
        Pane parent = (Pane) stage.getScene().getRoot();
        if (owner != null)
            for (Screen screen : Screen.getScreens()) {
                if (screen.getVisualBounds().contains(owner.getX(), owner.getY()))
                    rect = screen.getVisualBounds();

            }

        double x = rect.getMinX() + rect.getWidth() / 2 - width / 2;
        double y = rect.getMinY() + rect.getHeight() / 2 - height / 2;

        /*if(owner != null){
            x -= parent.getPrefWidth() / 2;
            y -= parent.getPrefHeight() / 2;
        }*/


        stage.setX(x);
        stage.setY(y);
    }

    public static void main(String[] args) {
        for(StackTraceElement stackTraceElement: Thread.currentThread().getStackTrace())
            System.out.println(stackTraceElement.getClassName() + "," + stackTraceElement.getMethodName());
    }
}
