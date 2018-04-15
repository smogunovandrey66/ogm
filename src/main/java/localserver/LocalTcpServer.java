package localserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static core.platformconst.PlatformConst.DEFAULT_TCP_PORT;

class LocalTcpServer extends Thread{
    private Logger logger;
    private ServerSocket serverSocket;
    private ArrayList<LocalSession> sessions;
    private LocalBlock localBlock;
    LocalTcpServer(LocalBlock localBlock){
        this.localBlock = localBlock;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(LocalTcpServer.class);
        sessions = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(DEFAULT_TCP_PORT);
        } catch (IOException e) {
            logger.error("Локальный сервер не создан: %s", e.getMessage());
            return;
        }

        logger.debug("Создан и запущен локальный TCP сервер");
        while (!interrupted()){
            try {
                Socket socket = serverSocket.accept();
                sessions.add(new LocalSession(socket));
            } catch (IOException e) {
                logger.error("Ошибка соединения с клиентом%s", e.getMessage());
            }
        }
    }

    private void checkAndMakePlaces(){
        File placesFile = new File("places");
        if(!(placesFile.exists() && !placesFile.isDirectory())){

        }
    }
}
