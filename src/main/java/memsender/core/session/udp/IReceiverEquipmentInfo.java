package memsender.core.session.udp;

public interface IReceiverEquipmentInfo {
    void update(String ip, int port, boolean avaliable, int idEq, int swEq, String strInfo);
}
