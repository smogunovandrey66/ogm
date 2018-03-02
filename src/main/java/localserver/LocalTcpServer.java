package localserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LocalTcpServer extends Thread{
    private Logger logger;

    LocalTcpServer(){
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(LocalTcpServer.class);
        logger.debug("Создан и запущен локальный TCP сервер");
        while (!interrupted()){
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
