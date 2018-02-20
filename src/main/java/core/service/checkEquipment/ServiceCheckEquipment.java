package core.service.checkEquipment;

import java.net.*;
import java.util.ArrayList;

public class ServiceCheckEquipment {
    private static ServiceCheckEquipment service = new ServiceCheckEquipment();
    private ArrayList<IListenEquipment> list;
    private StateCheckEquipment stateCheckEquipment;
    private Thread thread;
    private volatile String ip;
    private Object monitor;

    private ServiceCheckEquipment() {
        list = new ArrayList<>();
        stateCheckEquipment = new StateCheckEquipment(0, 0, new String[]{"Проверка оборудования..."});
        monitor = new Object();
    }

    public static void addView(IListenEquipment listener) {
        int idx = service.list.indexOf(listener);
        if (idx == -1) {
            service.list.add(listener);
            listener.updateStateCheckEquipment(service.stateCheckEquipment);
        }
    }

    public static void removeListener(IListenEquipment listener) {
        service.list.remove(listener);
    }

    public static void check(String ip) {
        if (service.thread == null) {
            service.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.interrupted()) {
                        try {
                            InetAddress inetAddress = InetAddress.getByName(ip);
                            DatagramPacket datagramPacket = new DatagramPacket(new byte[]{1}, 1, inetAddress, 18756);
                            DatagramSocket socket = new DatagramSocket();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (SocketException e) {

                        }

                        try {
                            service.monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            service.thread.start();
        }
    }
}
