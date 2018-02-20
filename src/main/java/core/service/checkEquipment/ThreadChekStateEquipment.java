package core.service.checkEquipment;

import java.net.*;

public class ThreadChekStateEquipment extends Thread{
    private String ip;
    private Object monitor;

    public ThreadChekStateEquipment(String ip) {
        super();
        this.ip = ip;
        monitor = new Object();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                InetAddress inetAddress = InetAddress.getByName(ip);
                DatagramPacket datagramPacket = new DatagramPacket(new byte[]{1}, 1, inetAddress, 18756);
                DatagramSocket socket = new DatagramSocket();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e){

            }
        }
    }
}
