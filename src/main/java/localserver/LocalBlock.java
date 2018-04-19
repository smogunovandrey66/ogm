package localserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class LocalBlock {
    public static final String BLOCK_PROPERTIES = "block.properties";
    public static final String ID_EQ = "idEq";
    public static final String SW_EQ = "swEq";
    public static final Path DEFAULT_PATH= Paths.get(System.getProperty("user.home"), "ogm", "localblocks");

    private String pathToBlock;
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

    public static void makeDeaultBlock(byte idEq, byte swEq, Path pathToBlock) throws IOException {
        Files.createDirectories(pathToBlock);
        Path pathToSettings = pathToBlock.resolve(BLOCK_PROPERTIES);
        Files.createFile(pathToSettings);
        Properties properties = new Properties();
        properties.setProperty(ID_EQ, String.valueOf(idEq & 0xFF));
        properties.setProperty(SW_EQ, String.valueOf(swEq & 0xFF));
        FileOutputStream fileOutputStream = new FileOutputStream(pathToSettings.toFile());
        properties.store(fileOutputStream, null);
        fileOutputStream.close();
    }

    public static void main(String[] args) {
        byte idEq = (byte) 140;
        byte swEq = (byte) 0;
        Path pathToBlock = DEFAULT_PATH.resolve(String.valueOf(idEq & 0xFF));
        try {
            makeDeaultBlock(idEq, swEq, pathToBlock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
