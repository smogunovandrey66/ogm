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
    public SessionClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }
    public void send(byte[] command){
        try {
            outputStream.write(command);
        } catch (IOException e) {
            ;
        }
    }

    public int read() throws IOException {
        return inputStream.read(buffer);
    }

}
