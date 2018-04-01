package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SessionServer extends Thread {
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] buffer = new byte[0xFFFF];
    private int sizeRead;
    private int lenPacket;
    private Logger logger;
    private boolean useCrc16;
    private boolean isAutorized;
    private String str;
    private int posBuffer;

    private void write(int oneByte){
        buffer[posBuffer] = (byte)oneByte;
        posBuffer++;
    }
    private void write(byte[] severalBytes){
        for(int i = 0; i < severalBytes.length; i++)
            write(severalBytes[i]);
    }

    public SessionServer(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        setDaemon(true);
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(SessionServer.class);
        logger.debug("Start sessionServer for:{}:{}", clientSocket.getInetAddress().getHostName(), clientSocket.getPort());
        try {
            while (true) {
                if (inputStream.read(buffer, 0, 2) != 2)
                    throw new SocketException("Некорректные пришедшие данные");
                lenPacket = buffer[0] + buffer[1] << 8;
                sizeRead = inputStream.read(buffer, 0, lenPacket);
                if (useCrc16) {

                }
                str = ServiceShare.bytesToHexString(buffer, 0, lenPacket, " ");
                logger.debug("recieve from:{}:{},sizeRead:{},lenPacket:{}",
                        clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort(),
                        sizeRead, lenPacket);
                logger.debug("recieved data:{}:{}", lenPacket, str);
                if (sizeRead != lenPacket)
                    throw new SocketException("Некорректные пришедшие данные");
                posBuffer = 0;
                switch (buffer[0]) {
                    case Commands.REGISTRATION:
                        write(0);
                        write(1);
                        write(Commands.REGISTRATION);
                        break;
                    default:
                        throw new SocketException("Необработанная команда");
                }
                outputStream.write(buffer, 0, posBuffer);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            try {
                write(0);
                write(1);
                write(233);
                outputStream.write(buffer, 0, 3);
                inputStream.close();
                outputStream.close();
                clientSocket.close();
            } catch (IOException e1) {
                logger.error(e1.getMessage());
            }
        }
    }
}
