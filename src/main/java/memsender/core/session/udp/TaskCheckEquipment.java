package memsender.core.session.udp;

public class TaskCheckEquipment {
    public String host;
    public int port;
    public IReceiverEquipmentInfo receiver;
    public TaskCheckEquipment build(String host){
        this.host = host;
        return this;
    }
    public TaskCheckEquipment build(int port){
        this.port = port;
        return  this;
    }
    public TaskCheckEquipment build(IReceiverEquipmentInfo receiver){
        this.receiver = receiver;
        return  this;
    }
}
