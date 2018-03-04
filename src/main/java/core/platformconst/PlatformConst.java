package core.platformconst;


import core.ServiceShare;

import java.util.Arrays;
import java.util.List;

public class PlatformConst {
    //Все мозможные выпуски
    public static final int[] MAJOR_VERSIONS = {1};
    public static final int[] MINOR_VERSIONS = {0};
    public static final int[] RELEASES = {0};
    public static final int[] BUILDS = {0xe6650b9};

    //Текущая версия - самая последняя
    public static final int MAJOR_VERSION = MAJOR_VERSIONS[0];
    public static final int MINOR_VERSION = MINOR_VERSIONS[0];
    public static final int RELEASE = RELEASES[0]; //https://www.koz1024.net/versioning/
    public static final int BUILD = BUILDS[0]; //c github'-а

    public static final int DEFAULT_TCP_PORT = 5050;

    public static final int COUNT_PLACES_140 = 24;
    public static final int ID_EQUIPMENT_140 = 140;
    public static final int[] SW_140 = new int[]{0};

    public static final int COUNT_PLACES_30 = 25;
    public static final int ID_EQUIPMENT_30 = 120;
    public static final int[] SW_30 = new int[]{1};

    public static final int COUNT_PLACES_8 = 24;
    public static final int ID_EQUIPMENT_8 = 8;
    public static final int[] SW_8 = new int[]{1,  2};

    public static int COUNT_PLACES ;
    public static int ID_EQUIPMENT;
    public static int SW;

    public static final int[] ID_EQUIPMENT_ALL = new int[]{ID_EQUIPMENT_8, ID_EQUIPMENT_30, ID_EQUIPMENT_140};

    public static String makeVersion() {
        return String.format("%d.%d.%d.%x", MAJOR_VERSION, MINOR_VERSION, RELEASE, BUILD);
    }

    public static void setEquipment(int id, int sw) throws UnsupportedEquipmentException {
        List<Integer> list;
        switch (id) {
            case ID_EQUIPMENT_8:
                if(!ServiceShare.contains(SW_8, sw))
                    throw new UnsupportedEquipmentException(id, sw);
                COUNT_PLACES = COUNT_PLACES_8;
                ID_EQUIPMENT = id;
                SW = sw;
                break;
            case ID_EQUIPMENT_30:
                if(!ServiceShare.contains(SW_30, sw))
                    throw new UnsupportedEquipmentException(id, sw);
                COUNT_PLACES = COUNT_PLACES_30;
                ID_EQUIPMENT = id;
                SW =  sw;
                break;
            case ID_EQUIPMENT_140:
                if(!ServiceShare.contains(SW_140, sw))
                    throw new UnsupportedEquipmentException(id, sw);
                COUNT_PLACES = COUNT_PLACES_140;
                ID_EQUIPMENT = id;
                SW = sw;
                break;
                default:
                    throw new UnsupportedEquipmentException(id, sw);
        }
    }

}
