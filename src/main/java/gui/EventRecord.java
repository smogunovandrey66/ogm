package gui;

import java.util.Date;

public class EventRecord{
    private TypeEvent typeEvent;
    private String message;
    private Date date;
    public EventRecord(TypeEvent typeEvent, String message){
        this.typeEvent = typeEvent;
        this.message = message;
        this.date = new Date();
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}