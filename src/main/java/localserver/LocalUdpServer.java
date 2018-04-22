package localserver;

import core.Commands;
import core.ServiceShare;
import core.ErrorsServer;
import core.service.equipment.ServiceEquipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

class LocalUdpServer extends Thread {

    private Logger logger;
    private DatagramSocket socket;
    private LocalBlock localBlock;
    private byte[] sucessResponse;
    private byte[] errorResponse;

    private byte[] getError(byte error){
        errorResponse[7] = error;
        return errorResponse;
    }

    private void makeSuccessResponse() throws IOException {
        int idEq = localBlock.getId();
        int swEq = localBlock.getSw();
        String middle = null;
        switch (idEq) {
            case LocalBlock.ID_140:
                middle = "АМД140\nНеобходимо оборудование КПО140";
                break;
            case LocalBlock.ID_8:
                middle = "ОГМ-8\nНеобходимо оборудование КПО-6";
                break;
            case LocalBlock.ID_30:
                middle = "ОГМ-30\nНеобходимо оборудование КПО-5";
                break;
                default:
                    throw new IOException("Неподдерживаемое оборудование");
        };
        String begin = new String(new byte[]{Commands.IDENTIFICATION_EQUIPMENT, (byte) idEq, (byte) swEq});
        String end = new String(new byte[]{0, 0, 0});
        sucessResponse = (begin + middle + end).getBytes("windows-1251");
    }

    LocalUdpServer(LocalBlock localBlock) throws IOException {
        this.localBlock = localBlock;
        makeSuccessResponse();
        errorResponse = ServiceLocalBlock.makeError(ErrorsServer.INVALID_ARGUMENTS);
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        logger = LoggerFactory.getLogger(LocalUdpServer.class);
        logger.debug("Создан и запущен локальный UDP сервер");
        try {
            socket = new DatagramSocket();
            localBlock.writeProperty(LocalBlock.UDP_PORT, String.valueOf(socket.getLocalPort()));
        } catch (SocketException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }

        if (socket == null)
            return;


        while (!interrupted()) {
            DatagramPacket receiveDatagramPacket = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(receiveDatagramPacket);
                logger.debug("Локальный Udp сервер получил: " + ServiceShare.bytesToHexString(receiveDatagramPacket.getData(), receiveDatagramPacket.getLength(), " "));
                byte[] sendData;
                if (receiveDatagramPacket.getLength() == 0)
                    sendData = errorResponse;
                else {
                    switch (receiveDatagramPacket.getData()[0]) {
                        case Commands.IDENTIFICATION_EQUIPMENT:
                            sendData = sucessResponse;
                            break;
                        default:
                            sendData = errorResponse;
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
