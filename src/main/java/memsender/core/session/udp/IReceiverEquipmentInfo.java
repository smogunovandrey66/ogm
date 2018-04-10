package memsender.core.session.udp;

public interface IReceiverEquipmentInfo {
    void update(String host, int port, boolean avaliable, byte idEq, byte swEq, String strInfo);
}
