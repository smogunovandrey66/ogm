package localserver;

import java.util.ArrayList;

public class LocalBlock {
    private String path;
    private byte id;
    private byte sw;
    private ArrayList<String> strInfo;
    private LocalTcpServer localTcpServer;
    private LocalUdpServer localUdpServer;

    public ArrayList<String> getStrInfo() {
        return strInfo;
    }

    public void setStrInfo(ArrayList<String> strInfo) {
        this.strInfo = strInfo;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public byte getSw() {
        return sw;
    }

    public void setSw(byte sw) {
        this.sw = sw;
    }

    public void start(){
        if(localTcpServer == null)
            localTcpServer = new LocalTcpServer(this);
        if(localUdpServer == null)
            localUdpServer = new LocalUdpServer(this);
    }

    public static void init(byte idEq){

    }
}
