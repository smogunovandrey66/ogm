package core.service.equipment;

import core.Command;
import core.ServiceShare;
import core.service.events.ServiceSettings;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.prefs.Preferences;


public class ServiceEquipment {
    class Task extends TimerTask {

        @Override
        public void run() {
//            ServiceEquipment.check(service.ip);
        }
    }

    class ThreadEquipment extends Thread {
        private Object monitor = new Object();
        private InetAddress inetAddress;
        private final EquipmentInfo equipmentInfo;
//        Timer timer;

        public ThreadEquipment(EquipmentInfo equipmentInfo) {
            setDaemon(true);
            this.equipmentInfo = equipmentInfo;
//            timer = new Timer();
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    //Ждём сигнала для старта проверки
                    synchronized (equipmentInfo) {
                        equipmentInfo.wait();
                    }
                    InetAddress inetAddress;

                    synchronized (equipmentInfo) {
                        if (equipmentInfo.getStateCheck() == StateCheck.NOTCORRECTIP) {
                            continue;
                        }
                        inetAddress = equipmentInfo.getIpChecking();
                    }

                    EquipmentInfo equipmentInfoNew = checkSync(inetAddress.getHostAddress());

                    synchronized (equipmentInfo) {
                        if (inetAddress.equals(equipmentInfo.getIpChecking())) {

                            equipmentInfo.setStateCheck(StateCheck.AVALIABLE);
                            //TODO установка информации из сокета
                            equipmentInfo.setIpChecked(equipmentInfo.getIpChecking());
                            equipmentInfo.setId(1);
                            equipmentInfo.setSw(1);
                            equipmentInfo.getInfo().clear();
                            equipmentInfo.getInfo().add("Good");
                            service.updateReceivers();
                        }
                    }


                } catch (InterruptedException e) {
                    synchronized (equipmentInfo) {
                        equipmentInfo.setStateCheck(StateCheck.CRASHSERVICE);
                        service.updateReceivers();
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Интервал опроса оборудования, <b>мс</b>
     */
    public static String INTERVAL_CHECK = "intervalCheck";
    public static String TIMEOUT_CHECK = "TIMEOUT_CHECK";

    private static ServiceEquipment service = new ServiceEquipment();
    private ArrayList<IReceiveEquipment> listReceivers;
    private final EquipmentInfo equipmentInfo;
    private ThreadEquipment thread;
    private InetAddress inetAddress;
    private long intervalCheck;

    private ServiceEquipment() {
        listReceivers = new ArrayList<>();
        equipmentInfo = new EquipmentInfo();
//        timer = new Timer();
        thread = new ThreadEquipment(equipmentInfo);
        thread.start();
        intervalCheck = Preferences.userRoot().node(getClass().getName()).getInt(INTERVAL_CHECK, 1500);
    }

    void updateReceivers() {
        for (IReceiveEquipment receiver : listReceivers)
            receiver.sendEquipment(equipmentInfo);
    }

    private void checkIpPrivate(String ipStr) {
        synchronized (equipmentInfo) {
            equipmentInfo.setIpStr(ipStr);
            try {
                equipmentInfo.setStateCheck(StateCheck.CHECKING);
                equipmentInfo.setIpChecking(ServiceShare.strToIp(ipStr));
                equipmentInfo.notify();
            } catch (UnknownHostException e) {
                equipmentInfo.setStateCheck(StateCheck.NOTCORRECTIP);
                e.printStackTrace();
            }
            updateReceivers();
        }
    }

    //---Статические методы---

    public static void addReceiveEquipment(IReceiveEquipment receiver) {
        if (service.listReceivers.indexOf(receiver) == -1)
            service.listReceivers.add(receiver);
    }

    public static void updateReceiver(IReceiveEquipment receiver) {
        receiver.sendEquipment(service.equipmentInfo);
    }

    public static void removeReceiveEquipment(IReceiveEquipment receiver) {
        service.listReceivers.remove(receiver);
    }

    public static void check(String ipStr) {
        service.checkIpPrivate(ipStr);
    }

    public static EquipmentInfo checkSync(String ipStr) {
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        DatagramSocket datagramSocket = null;
        try {
            try {
                equipmentInfo.setIpStr(ipStr);
                InetAddress inetAddress = ServiceShare.strToIp(ipStr);
                equipmentInfo.setIpChecking(inetAddress);
                //Отправляемый пакет
                DatagramPacket sendDatagramPacket = new DatagramPacket(new byte[]{Command.IDENTIFICATION}, 1, inetAddress, 18756);
                datagramSocket = new DatagramSocket();
                datagramSocket.setSoTimeout(ServiceSettings.root().node(ServiceEquipment.class.getName()).getInt(TIMEOUT_CHECK, 1500));
                datagramSocket.send(sendDatagramPacket);
                //Получаемый пакет
                DatagramPacket receiveDatagramPacket = new DatagramPacket(new byte[500], 500, InetAddress.getLocalHost(), datagramSocket.getLocalPort());
                datagramSocket.receive(receiveDatagramPacket);
                //Полученные данные ввиде байтов
                byte[] receiveData = receiveDatagramPacket.getData();
                //Первый байт всегда отправленная команда
                if (receiveData[0] == Command.IDENTIFICATION) {
                    //Пропускаем три байта
                    String[] strReceive = new String(receiveData, 3, receiveDatagramPacket.getLength() - 1, "windows-1251").split("\\n");
                    equipmentInfo.setId(receiveData[1] & 0xFF);
                    equipmentInfo.setSw(receiveData[2] & 0xFF);
                    //удаляем пробелы, знаки табуляции, символы перехода новой строки
                    for (int i = 0; i < strReceive.length; i++)
                        strReceive[i] = strReceive[i].trim();
                    equipmentInfo.setIpChecked(inetAddress);
                    equipmentInfo.setStateCheck(StateCheck.AVALIABLE);
                    equipmentInfo.setInfo(new ArrayList<>(Arrays.asList(strReceive)));
                }
            } catch (UnknownHostException e) {
                System.out.println("UnknownHostException");
                e.printStackTrace();
                equipmentInfo.setStateCheck(StateCheck.NOTCORRECTIP);
                equipmentInfo.setIpChecking(null);
                equipmentInfo.setIpChecked(null);
            } catch (SocketException e) {
                System.out.println("SocketException");
                e.printStackTrace();
                equipmentInfo.setIpChecking(null);
                equipmentInfo.setIpChecked(null);
            } catch (SocketTimeoutException e) {
                System.out.println("SocketTimeoutException");
                e.printStackTrace();
                equipmentInfo.setIpChecking(null);
                equipmentInfo.setIpChecked(null);
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
                equipmentInfo.setIpChecking(null);
                equipmentInfo.setIpChecked(null);
            }
        } finally {
            if (datagramSocket != null) {
                if (!datagramSocket.isClosed())
                    datagramSocket.close();
                datagramSocket = null;
            }
        }

        return equipmentInfo;
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("simple");
        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord logRecord) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        });
        Handler handlerEvents = new Handler() {
            @Override
            public void publish(LogRecord logRecord) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        Handler handlerLog = new Handler() {
            @Override
            public void publish(LogRecord logRecord) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        logger.addHandler(handlerEvents);
        logger.addHandler(handlerLog);
        logger.severe("Ошибка");
        check(null);
//        System.out.println(checkSync("192.168.248.221"));
    }
}
