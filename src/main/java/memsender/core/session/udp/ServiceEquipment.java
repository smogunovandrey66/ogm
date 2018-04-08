package memsender.core.session.udp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceEquipment extends Thread {
    private static ServiceEquipment serviceEquipment;

    private HashMap<String, HashMap<Integer, IReceiverEquipmentInfo>> h;
    private Lock lock;
    private Condition condition;


    private ServiceEquipment() {
        h = new HashMap<>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    @Override
    public void run() {
        serviceEquipment = new ServiceEquipment();
        while (true) {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void runCheck(String host, int port, IReceiverEquipmentInfo receiver) {
        serviceEquipment.lock.lock();
        serviceEquipment.h.containsKey(host);
    }

}
