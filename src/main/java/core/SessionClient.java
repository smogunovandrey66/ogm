package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;

public class SessionClient{
    private static Logger logger = LoggerFactory.getLogger(SessionClient.class);
    private Socket socket;
    private byte[] buffer = new byte[0xFFFF];
    private int posBuffer;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int size;
    private boolean useCrc16;
    public SessionClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }
    public void send(byte[] command){
        try {
            size = command.length & 0xFFFF;
            outputStream.write(size >> 8);
            outputStream.write(size & 0xFF);
            outputStream.write(command);
            if(useCrc16){

            }
            inputStream.read(buffer, 0, 3);
            size = buffer[0] << 8 + buffer[1];
            //Неверная команда
            if(buffer[2] != command[0]){

            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public int read() throws IOException {
        return inputStream.read(buffer);
    }

}
