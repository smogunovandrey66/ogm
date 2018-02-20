package core.service.checkEquipment;

public class StateCheckEquipment {
    private int id;
    private int sw;
    private String[] info;

    public StateCheckEquipment(int id, int sw, String[] info){
        this.id = id;
        this.sw = sw;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public int getSw() {
        return sw;
    }

    public String[] getInfo() {
        return info;
    }
}
