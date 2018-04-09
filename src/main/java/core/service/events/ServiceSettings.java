package core.service.events;


import core.platformconst.PlatformConst;

import java.util.prefs.Preferences;

/**
 * Сервис настроек приложения <font style = "color: red"><b>ОГМ</b></font>
 */
public class ServiceSettings {
    private static ServiceSettings service = new ServiceSettings();
    private Preferences preferences;
    private ServiceSettings(){
        preferences = Preferences.userRoot().node(PlatformConst.makeVersion());
    }

    public static Preferences pref(){
        return service.preferences;
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
}
