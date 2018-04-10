package memsender.core.session.udp;


import core.Commands;
import core.ServiceShare;
import core.ServiceSettings;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceEquipment extends Thread {
    private static ServiceEquipment serviceEquipment = null;
    private static Logger logger;

    private LinkedList<TaskCheckEquipment> queueTasks;
    private Lock lock;
    private Condition condition;
    private DatagramSocket datagramSocket;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private TaskCheckEquipment curTask;
    private String curHost;
    private int curPort;
    private IReceiverEquipmentInfo curReceiver;
    private boolean curAvaliable;
    private byte curIdEq, curSwEq;
    private String curStrInfo;
    /**
     * Максимоальное время ожидания при проверке оборудования, мс
     */
    public final static String TIMEOUT_CHECKEQUIPMENT = "TIMEOUT_CHECKEQUIPMENT";
    /**
     * Порт, на котором происходит проверка оборудования
     */
    public final static int DEFAULT_PORT_CHECK_EQUIPMENT = 18756;

    public static void init() {
        new ServiceEquipment();
    }


    private ServiceEquipment() {
        super();
        setDaemon(true);
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(ServiceSettings.pref(this).getInt(TIMEOUT_CHECKEQUIPMENT, 1500));
            ;
        } catch (SocketException e) {
            logger.error("Ошибка создания сервиса опроса оборудования: {}", e.getMessage());
            serviceEquipment = null;
            return;
        }
        queueTasks = new LinkedList<>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
        logger = LoggerFactory.getLogger(ServiceEquipment.class);
        receivePacket = new DatagramPacket(new byte[500], 500);
        sendPacket = new DatagramPacket(new byte[]{Commands.IDENTIFICATION_EQUIPMENT}, 1);
        serviceEquipment = this;
        start();
    }

    @Override
    public void run() {
        logger.debug("Запущен сервис проверки оборудования");

        while (true) {
            //Получаем первое задание на проверку
            lock.lock();
            try {
                while ((curTask = queueTasks.peek()) == null)
                    condition.await();
                curHost = curTask.host;
                curPort = curTask.port;
                curReceiver = curTask.receiver;
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                break;
            } finally {
                lock.unlock();
            }

            //Собственно проверка доступности оборудования
            curAvaliable = false;
            try {
                sendPacket.setAddress(InetAddress.getByName(curHost));
                sendPacket.setPort(curPort);
                datagramSocket.send(sendPacket);
                datagramSocket.receive(receivePacket);
                byte[] receiveBytes = receivePacket.getData();
                if (receiveBytes[0] == Commands.IDENTIFICATION_EQUIPMENT) {
                    curIdEq = receiveBytes[1];
                    curSwEq = receiveBytes[2];
                    curStrInfo = new String(receiveBytes, 3, receivePacket.getLength() - 1, "windows-1251").trim();
                    curAvaliable = true;
                }
            } catch (IOException ioE) {
                logger.debug(ioE.getMessage());
            } catch (ArrayIndexOutOfBoundsException arrException) {
                logger.debug(arrException.getMessage());
            }

            //Уведомить о состоянии проверки(при необходимости) и удалить(тоже при необходимости)
            lock.lock();
            try {
                if(queueTasks.indexOf(curTask) == -1)
                    continue;
                queueTasks.remove(curTask);
                curTask = null;
                TaskFxEquipment taskFxEquipment = new TaskFxEquipment();
                taskFxEquipment.avaliable = curAvaliable;
                taskFxEquipment.idEq = curIdEq;
                taskFxEquipment.swEq = curSwEq;
                taskFxEquipment.host = curHost;
                taskFxEquipment.port = curPort;
                taskFxEquipment.receiver = curReceiver;
                taskFxEquipment.strInfo = curStrInfo;

                Platform.runLater(taskFxEquipment);
            } finally {
                lock.unlock();
            }
        }
    }

    public static void runCheck(String host, int port, IReceiverEquipmentInfo receiver) {
        serviceEquipment.lock.lock();
        try {
            for(TaskCheckEquipment task: serviceEquipment.queueTasks){
                if(task.host.equals(host) & task.port == port & task.receiver == receiver) {
                    return;
                }
            }
            serviceEquipment.queueTasks.add(new TaskCheckEquipment().build(port).build(host).build(receiver));
            serviceEquipment.condition.signal();
        } finally {
            serviceEquipment.lock.unlock();
        }
    }

    public static void removeCheck(String host, int port, IReceiverEquipmentInfo receiver){
        serviceEquipment.lock.lock();
        try {
            Iterator<TaskCheckEquipment> iterator = serviceEquipment.queueTasks.iterator();
            TaskCheckEquipment task;
            while (iterator.hasNext()){
                task = iterator.next();
                if(task.host.equals(host) & task.port == port & task.receiver == receiver)
                    iterator.remove();
            }
        } finally {
            serviceEquipment.lock.unlock();
        }
    }

    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("one");
        linkedList.add("twho");
        linkedList.add("three");
        Iterator<String> iterator = linkedList.iterator();
        while (iterator.hasNext()){
            if(iterator.next().equals("one")){
                iterator.remove();
            }
        }
        System.out.println(linkedList);
//        for(String s: linkedList){
//            if(s.equals("one"))
//                linkedList.remove("one");
//        }
        init();
        JFXPanel jfxPanel = new JFXPanel();
        runCheck("192.168.5.49", DEFAULT_PORT_CHECK_EQUIPMENT, new IReceiverEquipmentInfo() {
            @Override
            public void update(String host, int port, boolean avaliable, byte idEq, byte swEq, String strInfo) {
                int i = idEq & 0xFF;
                System.out.println(String.format("%02X(%d), %02X(%d)", idEq, i, swEq, swEq));
                System.out.println(strInfo);
                for(String str: strInfo.split("\\n")) {
                    try {
                        System.out.println(ServiceShare.bytesToHexString(str.getBytes("windows-1251"), " "));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        IReceiverEquipmentInfo ir =  new IReceiverEquipmentInfo() {
            @Override
            public void update(String host, int port, boolean avaliable, byte idEq, byte swEq, String strInfo) {
                int i = idEq & 0xFF;
                System.out.println(String.format("%02X(%d), %02X(%d)", idEq, i, swEq, swEq));
                System.out.println(strInfo);
                for(String str: strInfo.split("\\n")) {
                    try {
                        System.out.println(ServiceShare.bytesToHexString(str.getBytes("windows-1251"), " "));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        runCheck("192.168.1.2", DEFAULT_PORT_CHECK_EQUIPMENT, ir);
//        removeCheck("192.168.1.2", DEFAULT_PORT_CHECK_EQUIPMENT, ir);
    }

}
