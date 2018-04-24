package core;


import core.platformconst.PlatformConst;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import localserver.LocalBlock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.sqlite.JDBC;

/**
 * Сервис настроек приложения <font style = "color: red"><b>ОГМ</b></font>
 */
public class ServiceSettings {

    public static final Path DEFAULT_PATH_OGM = Paths.get(System.getProperty("user.home")).resolve("ogm");
    public static final String PROPERTIES_FILE = "app.properties";
    public static final String DB = "jdbc:sqlite:db";
    private static ServiceSettings service = null;
    private Preferences preferences;
    private Properties properties;
    private Path pathProperties = Paths.get(PROPERTIES_FILE);
    private Connection connection;
    private Statement statement;
    private HashMap<Integer, Path> pathsLocalBlock;

    private ServiceSettings() throws IOException, SQLException, ClassNotFoundException {
        preferences = Preferences.userRoot().node(PlatformConst.makeVersion());
//        Class.forName("org.sqlite.JDBC") ;
        connection = JDBC.createConnection(DB, new Properties()); // DriverManager.getConnection(DB);

        statement = connection.createStatement();

        properties = new Properties();
        if (Files.exists(pathProperties))
            try (FileInputStream fileInputStream = new FileInputStream(Paths.get("").toFile())) {
                properties.load(fileInputStream);
            }
        pathsLocalBlock = new HashMap<>();
    }

    public final static String WIDTH = "WIDTH";
    public final static String HEIGHT = "HEIGHT";
    public final static String Y = "Y";
    public final static String X = "X";
    public final static String EXIST = "EXIST";

    public static Path pathToLocalBlock(int idEq) throws SQLException {
        synchronized (service) {
            Path result = service.pathsLocalBlock.get(idEq);
            if (result == null) {
                service.statement.execute("CREATE TABLE if not exists pathsToLocalBlock " +
                        "('idEq' INTEGER PRIMARY KEY, 'path' text);");
                ResultSet resultSet = service.statement.executeQuery("SELECT path FROM pathsToLocalBlock where idEq = " + String.valueOf(idEq));
                if (resultSet.next()) {
                    result = Paths.get(resultSet.getString(1));
                } else {
                    result = LocalBlock.pathDefaultBlock(idEq);
                    service.statement.execute("INSERT INTO pathsToLocalBlock ('idEq', 'path') VALUES (" + String.valueOf(idEq) + ",'" +
                            result.toAbsolutePath().toString() + "');");
                }
                service.pathsLocalBlock.put(idEq, result);
            }
            return result;
        }
    }

    public static Preferences pref() {
        return service.preferences;
    }

    public static void init() throws IOException, SQLException, ClassNotFoundException {
        if (service == null)
            service = new ServiceSettings();
    }

    public static Preferences pref(String id) {
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
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace())
            System.out.println(stackTraceElement.getClassName() + "," + stackTraceElement.getMethodName());
    }
}
