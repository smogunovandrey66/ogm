package core.service.equipment;

import core.ServiceShare;
import core.ServiceSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ServiceEquipment extends Thread {
    private final Queue<InetAddress> queueCheckingIp;
    private final ArrayList<IReceiveEquipment> listReceivers;
    private static Queue<InetAddress> queueCheckingIpStatic;
    private static ArrayList<IReceiveEquipment> listReceiversStatic;
    private final static Logger logger = LoggerFactory.getLogger(ServiceEquipment.class);

    /**
     * Команда запроса идентификации оборудования
     */
    public final static byte COMMAND_IDENTIFICATION = 0x01;
    /**
     * Порт, на котором происходит проверка оборудования
     */
    public final static int PORT_CHECK_EQUIPMENT = 18756;
    /**
     * Максимоальное время ожидания при проверке оборудования, мс
     */
    public final static String TIMEOUT_CHECKEQUIPMENT = "TIMEOUT_CHECKEQUIPMENT";

    public final static ServiceEquipment service = new ServiceEquipment();

    private ServiceEquipment() {
        queueCheckingIp = new LinkedList<>();
        queueCheckingIpStatic = queueCheckingIp;
        listReceivers = new ArrayList<>();
        listReceiversStatic = listReceivers;
        setDaemon(true);
        start();
        logger.debug("Создана служба проверки оборудования");
    }

    @Override
    public void run() {
        while (!interrupted()) {
            InetAddress checkingIp = null;
            EquipmentInfo equipmentInfo = new EquipmentInfo();

            try {
                synchronized (queueCheckingIp) {
                    if (queueCheckingIp.size() > 0) {
                        checkingIp = queueCheckingIp.remove();
                    } else {
                        queueCheckingIp.wait();
                        continue;
                    }
                }

                equipmentInfo.setIpChecking(checkingIp);

                //Отправляем запрос к оборудованию
                DatagramPacket sendDatagramPacket = new DatagramPacket(new byte[]{COMMAND_IDENTIFICATION}, 1, checkingIp, PORT_CHECK_EQUIPMENT);
                DatagramSocket socket = new DatagramSocket();
                socket.setSoTimeout(ServiceSettings.pref().node(ServiceEquipment.class.getName()).getInt(TIMEOUT_CHECKEQUIPMENT, 1500));
                socket.send(sendDatagramPacket);

                //Анализируем полученные данные
                DatagramPacket receiveDatagramPacket = new DatagramPacket(new byte[500], 500);
                socket.receive(receiveDatagramPacket);
                byte[] receiveBytes = receiveDatagramPacket.getData();
                if (receiveBytes[0] == COMMAND_IDENTIFICATION) {
                    String[] receiveString = new String(receiveBytes, 3, receiveDatagramPacket.getLength() - 1, "windows-1251").split("\\n");
                    equipmentInfo.setId(receiveBytes[1] & 0xFF);
                    equipmentInfo.setSw(receiveBytes[2] & 0xFF);
                    for (String str : receiveString)
                        equipmentInfo.getInfo().add(str.trim());
                    equipmentInfo.setIpChecked(checkingIp);
                    equipmentInfo.setStateCheck(StateCheck.AVALIABLE);
                }


            } catch (InterruptedException e) {
                logger.error(String.format("%s", e.getMessage()));
            } catch (SocketException e) {
                logger.error(String.format("%s", e.getMessage()));
            } catch (IOException e) {
                logger.error(String.format("%s", e.getMessage()));
            } finally {

            }

            synchronized (listReceivers) {
                for (IReceiveEquipment receiver : listReceivers) {
                    receiver.sendEquipment(equipmentInfo);
                }
            }
        }
    }

    public static void checkEquipment(InetAddress inetAddress) {
        synchronized (queueCheckingIpStatic) {
            queueCheckingIpStatic.add(inetAddress);
            queueCheckingIpStatic.notify();
        }
    }

    public static void addReceiver(IReceiveEquipment receiver) {
        synchronized (listReceiversStatic) {
            listReceiversStatic.add(receiver);
        }
    }

    public static void removeReceiver(IReceiveEquipment receiver) {
        synchronized (listReceiversStatic) {
            listReceiversStatic.remove(receiver);
        }
    }

    public static void main(String[] args) {
        IReceiveEquipment receiver = new IReceiveEquipment() {
            @Override
            public void sendEquipment(EquipmentInfo info) {
                logger.debug(String.format("Результат проверки оборудования: %s", info.toString()));
            }
        };
        addReceiver(receiver);
        String[] listIp = new String[]{"192.168.248.220", "192.168.1.2", "192.166.5.248", "192.168.0.155", "192.168.0.117"};
        try {
            checkEquipment(ServiceShare.strToIp("192.168.5.48"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeReceiver(receiver);
    }
}
