package core.service.connection;

import core.ServiceShare;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ServiceConnect {
    private static ServiceConnect service = null;

    /**
     * Состояние приложения: <ul><li>не подключено</li> <li>происходит подключение к блоку</li> <li>подключено к блоку</li>
     * <li>происходит подключение в автономном режиме</li> <li>подключено в автономном режиме</li></ul>
     */
    private StateConnection stateConnection;

    private ArrayList<IViewConnect> iViewConnects;

    private ServiceConnect() {
        stateConnection = StateConnection.off;
        iViewConnects = new ArrayList<>();
    }

    public static void checkInfoEquipment(String ip) {
        try {
            InetAddress inetAddress = ServiceShare.strToIp(ip);
            System.out.println(inetAddress.getHostAddress());
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(3000);
            DatagramPacket datagramPacket = new DatagramPacket(new byte[]{1}, 1, inetAddress, 18756);
            socket.send(datagramPacket);
            datagramPacket = new DatagramPacket(new byte[1500], 1500);
            socket.receive(datagramPacket);
            int id = datagramPacket.getData()[1];
            String str = new String(datagramPacket.getData(), 0, datagramPacket.getLength() - 1, "cp1251");
            String[] arrStr = str.split("\n");
            System.out.println(str);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        } catch (SocketTimeoutException e) {
            System.out.println("Время истекло");
        } catch (IOException e) {
            System.out.println("Ошибка сокета");
        }
    }

    public static void addIViewConnect(IViewConnect iViewConnect){
        service.iViewConnects.add(iViewConnect);
        iViewConnect.updateStateConnection(service.stateConnection);
    }

    public static void init(){
        if(service == null)
            service = new ServiceConnect();
    }
}
