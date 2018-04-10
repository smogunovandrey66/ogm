package memsender.core.session.udp;

class TaskFxEquipment implements Runnable {
    String host;
    int port;
    boolean avaliable;
    byte idEq;
    byte swEq;
    String strInfo;
    IReceiverEquipmentInfo receiver;

    @Override
    public void run() {
        receiver.update(host, port, avaliable, idEq, swEq, strInfo);
    }
}
