package localserver;

import core.ServiceSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LocalBlock {
    public static final String BLOCK_PROPERTIES = "block.properties";
    public static final String ID_EQ = "idEq";
    public static final int ID_140 = 140;
    public static final int ID_8 = 8;
    public static final int ID_30 = 30;
    public static final String SW_EQ = "swEq";
    public static final String TCP_PORT = "tcpPort";
    public static final String UDP_PORT = "udpPort";
    public static final String DEFAULT_NAME = "default";
    public static final String LOCAL_BLOCKS = "localblocks";


    private Path pathToBlock;
    private Integer id;
    private Integer sw;
    private LocalTcpServer localTcpServer;
    private LocalUdpServer localUdpServer;
    private Properties properties;
    private FileOutputStream fileOutputStream;

    public LocalBlock(){
    }

    public Path getPathToBlock() {
        return pathToBlock;
    }

    public synchronized void writeProperty(String key, String value) throws IOException {
        properties.put(key, value);
        try(FileOutputStream fileOutputStream = new FileOutputStream(pathToBlock.toFile())) {
            properties.store(fileOutputStream, "");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSw() {
        return sw;
    }

    public void setSw(int sw) {
        this.sw = sw;
    }

    public void start() throws InterruptedException, IOException {
//        if (localTcpServer == null) {
//            localTcpServer = new LocalTcpServer(this);
//            localTcpServer.join();
//        }
        if (localUdpServer == null) {
            localUdpServer = new LocalUdpServer(this);
            localUdpServer.join();
        }
    }

    public void load(Path path) throws IOException {
        properties = readProperties(path);
        id = Integer.valueOf(properties.getProperty(ID_EQ));
        sw = Integer.valueOf(properties.getProperty(SW_EQ));
        pathToBlock = path;
        fileOutputStream = new FileOutputStream(path.toFile());
        StringBuilder stringBuilder = new StringBuilder();
        switch (id){
            case ID_140:
        }
    }

    public static Properties readProperties(Path path) throws IOException {
        Properties prop = new Properties();
        FileInputStream fileInputStream = new FileInputStream(path.toFile());
        prop.load(fileInputStream);
        fileInputStream.close();
        return prop;
    }

    public static void makeDefaultBlock(int idEq, int swEq, Path pathToBlock) throws IOException {
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

    public static void makeDefaultBlock(int idEq, int swEq) throws IOException {
        makeDefaultBlock(idEq, swEq, pathDefaultBlock(idEq));
    }

    public static Path pathDefaultBlock(int idEq) {
        return ServiceSettings.DEFAULT_PATH_OGM.resolve(LOCAL_BLOCKS).resolve(String.valueOf(idEq & 0xFF)).resolve(DEFAULT_NAME);
    }

    public static boolean existDefaultFile(int idEq) {
        return Files.exists(pathDefaultProperties(idEq));
    }

    public static Path pathDefaultProperties(int idEq) {
        return pathDefaultBlock(idEq).resolve(BLOCK_PROPERTIES);
    }

    public static Properties readDefaultProp(int idEq) throws IOException {
        Properties prop = new Properties();
        FileInputStream fileInputStream = new FileInputStream(pathDefaultProperties(idEq).toFile());
        prop.load(fileInputStream);
        fileInputStream.close();
        return prop;
    }

    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ClassNotFoundException {
        ServiceSettings.init();
        int idEq = 30;
        int swEq = 0;

        LocalBlock localBlock = new LocalBlock();
        Path path = ServiceSettings.pathToLocalBlock(idEq);
        if(!Files.exists(path.resolve(BLOCK_PROPERTIES)))
            makeDefaultBlock(idEq, swEq, path);
        localBlock.load(path.resolve(BLOCK_PROPERTIES));
        localBlock.start();
    }
}
