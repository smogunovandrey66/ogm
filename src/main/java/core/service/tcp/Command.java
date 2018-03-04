package core.service.tcp;

public class Command {
    public byte[] sendData;
    public IReceiveCommand receiver;

    public Command(byte[] sendData, IReceiveCommand receiver){
        this.sendData = sendData;
        this.receiver = receiver;
    }

    public Command(byte[] sendData){
        this(sendData , null);
    }
}
