package memsender.core.session.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Session {
    private DatagramSocket datagramSocket;

    public Session(String host, int port) throws IOException {
        datagramSocket = new DatagramSocket(port, InetAddress.getByName(host));
    }
    public boolean avaliable(){
        return true;
    }
}
