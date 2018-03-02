package localserver;

public class ServiceLocalServer {
    private LocalUdpServer udpServer;
    private LocalTcpServer tcpServer;
    private static ServiceLocalServer service = null;

    private ServiceLocalServer(){
        udpServer = new LocalUdpServer();
        tcpServer = new LocalTcpServer();
    }

    static byte[] makeError(byte error){
        byte[] sendData = new byte[8];
        sendData[0] = 0x00;
        sendData[1] = 0x06;
        sendData[2] = error;
        for (int i = 3; i <= 6; i++)
            sendData[i] = 0x00;
        sendData[7] = 0x16;
        return sendData;
    }

    public static void init(){
        service = new ServiceLocalServer();
    }
}
