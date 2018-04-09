package memsender.core.session.udp;

import javafx.application.Platform;

public class TaskFxEquipment implements Runnable{
    public String ip;
    public int port;
    public boolean avaliable;
    public int idEq;
    public int swEq;
    public String strInfo;
    public IReceiverEquipmentInfo receiver;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        receiver.update(ip, port, avaliable, idEq, swEq, strInfo);
    }
}
