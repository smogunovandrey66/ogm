package localserver;

import core.ErrorsServer;
import core.platformconst.PlatformConst;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceLocalBlock {
    private LocalUdpServer udpServer;
    private LocalTcpServer tcpServer;

    private static ServiceLocalBlock service = null;

    private static HashMap<Byte, LocalBlock> blocks = new HashMap<>();

    private ServiceLocalBlock(){

    }

    static byte[] makeError(byte error){
        byte[] sendData = new byte[8];
        sendData[0] = 0x00;
        sendData[1] = 0x06;
        sendData[2] = ErrorsServer.ERROR_MESSAGE;
        for (int i = 3; i <= 6; i++)
            sendData[i] = 0x00;
        sendData[7] = error;
        return sendData;
    }

    public static void setCurrent(byte idEq){
        if(blocks.containsKey(idEq))
            return;
        LocalBlock localBlock = new LocalBlock();
    }
}
