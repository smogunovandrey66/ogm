package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ManagerSessionServer extends Thread {
    private static Logger logger;
    private List<SessionServer> listSessionServer = new ArrayList<>();
    public ServerSocket serverSocket;

    public ManagerSessionServer() throws IOException {
        serverSocket = new ServerSocket(5050);
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(ManagerSessionServer.class);
        logger.debug("ManagerSessionServer started");
        while (true)
            try {
                listSessionServer.add(new SessionServer(serverSocket.accept()));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
    }

    public static void main(String[] args) {
        try {
            new ManagerSessionServer().start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
