package core;


public class PlatformConst {
    public static int MAJOR_VERSION = 1;
    public static int MINOR_VERSION = 0;
    public static int RELEASE = 0; //https://www.koz1024.net/versioning/
    public static int BUILD = 0xe6650b9; //c github'-а

    public static String makeVersion(){
        return String.format("%d.%d.%d.%x", MAJOR_VERSION, MINOR_VERSION, RELEASE, BUILD);
    }
}
