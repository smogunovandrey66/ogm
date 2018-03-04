package core.platformconst;

public class UnsupportedEquipmentException extends Exception{
    private int id;
    private int sw;
    public UnsupportedEquipmentException(int id, int sw){
        super();
        this.id = id;
        this.sw = sw;
    }

    @Override
    public String getMessage() {
        return String.format("Неподдерживаемое оборудование: id=%d, sw=%d", id, sw);
    }
}
