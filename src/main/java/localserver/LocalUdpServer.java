package localserver;

import core.Commands;
import core.ServiceShare;
import core.ErrorsServer;
import core.service.equipment.ServiceEquipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

class LocalUdpServer extends Thread {

    private Logger logger;
    private DatagramSocket socket;
    private LocalBlock localBlock;

    LocalUdpServer(LocalBlock localBlock) {
        this.localBlock = localBlock;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(LocalUdpServer.class);
        logger.debug("Создан и запущен локальный UDP сервер");
        try {
            socket = new DatagramSocket(ServiceEquipment.PORT_CHECK_EQUIPMENT, InetAddress.getByName("localhost"));
        } catch (SocketException e) {
            logger.error(e.getMessage());
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        if (socket == null)
            return;

        while (!interrupted()) {
            DatagramPacket receiveDatagramPacket = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(receiveDatagramPacket);
                logger.debug("Локальный Udp сервере получил: " + ServiceShare.bytesToHexString(receiveDatagramPacket.getData(), receiveDatagramPacket.getLength(), " "));
                byte[] sendData;
                if (receiveDatagramPacket.getLength() == 0)
                    sendData = ServiceLocalBlock.makeError(ErrorsServer.INVALID_ARGUMENTS);
                else {
                    switch (receiveDatagramPacket.getData()[0]) {
                        case Commands.IDENTIFICATION_EQUIPMENT:
                            String str = new String(new byte[]{Commands.IDENTIFICATION_EQUIPMENT/*cmd*/, (byte) 140/*id*/, 0/*SW*/, 0/*разделитель*/}, "windows-1251") +
                                    "АМД140\n" + "Необходимо оборудование КПО140" +
                                    new String(new byte[]{0}, "windows-1251");
                            sendData = str.getBytes("windows-1251");
                            break;
                        default:
                            sendData = ServiceLocalBlock.makeError(ErrorsServer.INVALID_ARGUMENTS);
                    }
                }
                ;
                socket.send(new DatagramPacket(sendData, sendData.length, receiveDatagramPacket.getAddress(), receiveDatagramPacket.getPort()));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            receiveDatagramPacket = null;
        }
    }


    public static void main(String[] args) {

//        try {
//
//            while (true) {
//                socket.receive(receivePacket);
//                byte[] receiveData = receivePacket.getData();
//
//                System.out.println("LocalUdpServer recieve:");
//                for(int i = 0; i < receivePacket.getLength(); i++)
//                    System.out.print(String.format("%h ", receiveData[i]));
//                System.out.println();
//
//                byte[] sendData;
//
//                if(receivePacket.getLength() == 0){
//                    sendData = makeSendError();
//                } else{
//                    switch (receiveData[0]){
//                        case Command.IDENTIFICATION:
//                            String str = new String(new byte[]{1/*cmd*/, (byte)140/*id*/, 0/*SW*/, 0/*разделитель*/}, "windows-1251") +
//                                    "АМД140\n" + "Необходимо оборудование КПО140" +
//                                    new String(new byte[]{0}, "windows-1251");
//                            sendData = str.getBytes("windows-1251");
//                            System.out.println("lenght:" + sendData.length);
//                            break;
//                            default:
//                                sendData = makeSendError();
//                    }
//                }
//
//               socket.send(new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort()));
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
