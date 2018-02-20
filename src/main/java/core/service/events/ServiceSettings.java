package core.service.events;


import core.PlatformConst;

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

    public static Preferences root(){
        return service.preferences;
    }
}
