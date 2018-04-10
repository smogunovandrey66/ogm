package core.service.events;


import core.platformconst.PlatformConst;
import javafx.stage.Stage;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Сервис настроек приложения <font style = "color: red"><b>ОГМ</b></font>
 */
public class ServiceSettings {
    private static ServiceSettings service = null;
    private Preferences preferences;
    private ServiceSettings(){
        preferences = Preferences.userRoot().node(PlatformConst.makeVersion());
    }

    public final static String WIDTH = "WIDTH";
    public final static String HEIGHT = "HEIGHT";
    public final static String TOP = "TOP";
    public final static String LEFT = "LEFT";

    public static Preferences pref(){
        return service.preferences;
    }
    public static void init(){
        service = new ServiceSettings();
    }

    public static Preferences pref(String id){
        return service.preferences.node(id);
    }
    public static Preferences pref(Class<?> clazz){
        return pref(clazz.getName());
    }
    public static Preferences pref(Object object){
        return pref(object.getClass());
    }

    public static Preferences node(String id){
        return service.preferences.node(id);
    }

    public static Preferences node(Class<?> clazz){
        return service.preferences.node(clazz.getName());
    }

    public static Preferences node(Object object){
        return node(object.getClass());
    }
    public static void loadDimensions(Stage stage, String id){
        Preferences pref = ServiceSettings.pref(id);
    }
}
