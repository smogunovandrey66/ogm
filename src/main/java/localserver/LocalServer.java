package localserver;

import core.Command;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class LocalServer {
    private static byte[] makeSendError(){
        byte[] sendData = new byte[8];
        sendData[0] = 0x0A;
        for (int i = 1; i <= 6; i++)
            sendData[i] = 0x00;
        sendData[7] = 0x16;
        return sendData;
    }
    public static void main(String[] args) {
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
        try {
            DatagramSocket socket = new DatagramSocket(18756, InetAddress.getByName("localhost"));
            while (true) {
                socket.receive(receivePacket);
                byte[] receiveData = receivePacket.getData();

                System.out.println("LocalServer recieve:");
                for(int i = 0; i < receivePacket.getLength(); i++)
                    System.out.print(String.format("%h ", receiveData[i]));
                System.out.println();

                byte[] sendData;

                if(receivePacket.getLength() == 0){
                    sendData = makeSendError();
                } else{
                    switch (receiveData[0]){
                        case Command.IDENTIFICATION:
                            String str = new String(new byte[]{1/*cmd*/, (byte)140/*id*/, 0/*SW*/, 0/*разделитель*/}, "windows-1251") +
                                    "АМД140\n" + "Необходимо оборудование КПО140" +
                                    new String(new byte[]{0}, "windows-1251");
                            sendData = str.getBytes("windows-1251");
                            System.out.println("lenght:" + sendData.length);
                            break;
                            default:
                                sendData = makeSendError();
                    }
                }

               socket.send(new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort()));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
