package memsender.core.session.udp;

class TaskCheckEquipment {
    String host;
    int port;
    IReceiverEquipmentInfo receiver;

    TaskCheckEquipment build(String host) {
        this.host = host;
        return this;
    }

    TaskCheckEquipment build(int port) {
        this.port = port;
        return this;
    }

    TaskCheckEquipment build(IReceiverEquipmentInfo receiver) {
        this.receiver = receiver;
        return this;
    }
}
