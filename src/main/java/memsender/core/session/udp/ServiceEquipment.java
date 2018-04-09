package memsender.core.session.udp;


import core.Commands;
import core.ServiceShare;
import core.service.events.ServiceSettings;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceEquipment extends Thread {
    private static ServiceEquipment serviceEquipment = null;
    private static Logger logger;

    private HashMap<String, HashMap<Integer, IReceiverEquipmentInfo>> tasks;
    private Queue<TaskCheckEquipment> queueTasks;
    private Lock lock;
    private Condition condition;
    private DatagramSocket datagramSocket;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private boolean error;
    /**
     * Максимоальное время ожидания при проверке оборудования, мс
     */
    public final static String TIMEOUT_CHECKEQUIPMENT = "TIMEOUT_CHECKEQUIPMENT";

    public void init() {
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
        tasks = new HashMap<>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
        logger = LoggerFactory.getLogger(ServiceEquipment.class);
        receivePacket = new DatagramPacket(new byte[500], 500);
        sendPacket = new DatagramPacket(new byte[]{Commands.IDENTIFICATION_EQUIPMENT}, 1);
        serviceEquipment = this;
        start();
    }

    private void removeNotSafe(String host, int port, IReceiverEquipmentInfo receiver) {
        HashMap<Integer, IReceiverEquipmentInfo> ports = tasks.get(host);
        if (ports != null) {
            IReceiverEquipmentInfo receiverEquipmentInfo = ports.get(port);
            if (receiverEquipmentInfo == receiver) {
                ports.remove(port);
                if (ports.size() == 0)
                    tasks.remove(host);
            }
        }
    }

    @Override
    public void run() {
        HashMap<Integer, IReceiverEquipmentInfo> ports;
        String host;
        int port;
        IReceiverEquipmentInfo receiver;

        logger.debug("Запущен сервис проверки оборудования");

        while (true) {
            lock.lock();
            try {/*
                if (tasks.size() == 0)
                    condition.await();
                if (!tasks.entrySet().iterator().hasNext())
                    continue;
                Map.Entry<String, HashMap<Integer, IReceiverEquipmentInfo>> entryHost = tasks.entrySet().iterator().next();
                host = entryHost.getKey();
                ports = entryHost.getValue();
                Map.Entry<Integer, IReceiverEquipmentInfo> entryPort = ports.entrySet().iterator().next();
                port = entryPort.getKey();
                receiver = entryPort.getValue();
            } catch (InterruptedException e) {
                break;*/
            } finally {
                lock.unlock();
            }
            //Собственно проверка доступности оборудования
            boolean avaliable = false;
            int idEq = 0, swEq = 0;
            String strInfo = "";
            try {
                datagramSocket.send(sendPacket);
                datagramSocket.receive(receivePacket);
                byte[] receiveBytes = receivePacket.getData();
                if (receiveBytes[0] == Commands.IDENTIFICATION_EQUIPMENT) {
                    idEq = receiveBytes[1];
                    swEq = receiveBytes[2];
                    strInfo = new String(receiveBytes, 3, receivePacket.getLength() - 1, "windows-1251");
                }
            } catch (IOException ioE) {
                logger.error(ioE.getMessage());
            } catch (ArrayIndexOutOfBoundsException arrException) {
                logger.error(arrException.getMessage());
            }

            //Уведомить о состоянии проверки(при необходимости) и удалить(тоже при необходимости)
            lock.lock();
            try {
                if(receiver != null){
                   receiver.update(host, port, avaliable, idEq, swEq, strInfo);
                   TaskFxEquipment later = new TaskFxEquipment();
                   later.avaliable = avaliable;
                   Platform.runLater(later);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void runCheck(String host, int port, IReceiverEquipmentInfo receiver) {
        serviceEquipment.lock.lock();
        try {/*
            HashMap<Integer, IReceiverEquipmentInfo> ports = serviceEquipment.tasks.get(host);
            if (ports != null) {
                IReceiverEquipmentInfo receiverEquipmentInfo = ports.get(port);
                if (receiverEquipmentInfo == receiver) {
                    return;
                } else {
                    ports.put(port, receiver);
                }
            } else {
                ports = new HashMap<>();
                ports.put(port, receiver);
                serviceEquipment.tasks.put(host, ports);
            }
            serviceEquipment.condition.signal();*/
            for(TaskCheckEquipment task: serviceEquipment.queueTasks){
                if(task.host == host & task.port == port & task.receiver == receiver) {
//                    serviceEquipment.condition.signal();
                    return;
                }

            }
            serviceEquipment.queueTasks.add(new TaskCheckEquipment().build(port).build(host).build(receiver));
            serviceEquipment.condition.signal();
        } finally {
            serviceEquipment.lock.unlock();
        }
    }

    public static void removeTask(String host, int port, IReceiverEquipmentInfo receiver) {
        serviceEquipment.lock.lock();
        try {
            serviceEquipment.removeNotSafe(host, port, receiver);
        } finally {
            serviceEquipment.lock.unlock();
        }
    }

    public static void test(){
        try{
            int i = 1 / 0;
            if(1 > 0)
                return;
        }finally {
            System.out.println("heelo");
        }
    }
    public static void main(String[] args) {
        test();
    }

}
