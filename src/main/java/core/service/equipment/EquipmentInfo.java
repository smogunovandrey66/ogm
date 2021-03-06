package core.service.equipment;

import java.net.InetAddress;
import java.util.ArrayList;

public class EquipmentInfo {
    private int id;
    private int sw;
    private ArrayList<String> info;
    private StateCheck stateCheck;
    private InetAddress ipChecking;
    private InetAddress ipChecked;

    public EquipmentInfo() {
        this.id = -1;
        this.sw = -1;
        this.info = new ArrayList<>();
        this.stateCheck = StateCheck.NOTAVALIABLE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSw() {
        return sw;
    }

    public void setSw(int sw) {
        this.sw = sw;
    }

    public ArrayList<String> getInfo() {
        return info;
    }

    public StateCheck getStateCheck() {
        return stateCheck;
    }

    public void setStateCheck(StateCheck stateCheck) {
        this.stateCheck = stateCheck;
    }

    public InetAddress getIpChecking() {
        return ipChecking;
    }

    public void setIpChecking(InetAddress ipChecking) {
        this.ipChecking = ipChecking;
    }

    public InetAddress getIpChecked() {
        return ipChecked;
    }

    public void setIpChecked(InetAddress ipChecked) {
        this.ipChecked = ipChecked;
    }

    @Override
    public String toString() {
        return String.format("id=%d,sw=%d,info=%s,stateChek=%s,ipChecking=%s,ipChecked=%s",
                id, sw, info.toString(), stateCheck.name(), ipChecking == null ? null :ipChecking.getHostAddress(),
                ipChecked == null ? null : ipChecked.getHostAddress());
    }
}
